package com.wsr

import androidx.compose.runtime.Composable
import com.wsr.ai.Assistant
import com.wsr.ai.Content
import com.wsr.ai.Part
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberAssistantPresenter(
    controller: MemoController = koinInject(),
): AssistantPresenter = rememberPresenter {
    AssistantPresenter(controller = controller)
}

internal class AssistantPresenter(
    private val controller: MemoController,
) : Presenter<AssistantUiState, UiEvent>(AssistantUiState()) {

    private lateinit var assistant: Assistant

    override fun onRemembered() {
        super.onRemembered()
        scope.launch {
            assistant = Assistant.create(controller)
        }
    }

    fun onChangeInput(input: String) {
        uiState = uiState.copy(input = input)
    }

    fun onClickSend() {
        val message = uiState.input
        val history = uiState.messages
        uiState = uiState.copy(input = "", messages = history + MessageUiState.User(message))
        scope.launch {
            val messages = assistant.send(
                message = message,
                history = history.map { it.toContent() },
            )
            uiState = uiState.copy(input = "", messages = messages.map { MessageUiState.from(it) })
        }
    }
}

internal data class AssistantUiState(
    val messages: List<MessageUiState> = emptyList(),
    val input: String = "",
) : UiState

internal sealed interface MessageUiState {
    fun toContent(): Content

    data class User(val text: String) : MessageUiState {
        override fun toContent(): Content = Content.User(part = Part.Text(text))
    }

    data class Tool(val name: String, val result: String) : MessageUiState {
        override fun toContent(): Content = Content.Tool(name = name, result = result)
    }

    data class AI(val text: String) : MessageUiState {
        override fun toContent(): Content = Content.AI(part = Part.Text(text))
    }

    companion object {
        fun from(content: Content): MessageUiState {
            val part = when (content.part) {
                is Part.Text -> (content.part as Part.Text).value
            }
            return when (content) {
                is Content.User -> User(part)
                is Content.Tool -> Tool(content.name, content.result)
                is Content.AI -> AI(part)
            }
        }
    }
}
