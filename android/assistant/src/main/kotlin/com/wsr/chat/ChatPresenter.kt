package com.wsr.chat

import androidx.compose.runtime.Composable
import com.wsr.MemoController
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import com.wsr.chat.ai.Assistant
import com.wsr.chat.ai.Content
import com.wsr.chat.ai.Part
import com.wsr.rememberPresenter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberChatPresenter(controller: MemoController = koinInject()): ChatPresenter =
    rememberPresenter {
        ChatPresenter(controller = controller)
    }

internal class ChatPresenter(
    private val controller: MemoController,
) : Presenter<ChatUiState, UiEvent>(ChatUiState()) {

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
        uiState = uiState.copy(input = "", messages = history + ChatMessageUiState.User(message))
        scope.launch {
            val messages = assistant.send(
                message = message,
                history = history.map { it.toContent() },
            )
            uiState = uiState.copy(
                input = "",
                messages = messages.map { ChatMessageUiState.from(it) },
            )
        }
    }
}

internal data class ChatUiState(
    val messages: List<ChatMessageUiState> = emptyList(),
    val input: String = "",
) : UiState

internal sealed interface ChatMessageUiState {
    fun toContent(): Content

    data class User(val text: String) : ChatMessageUiState {
        override fun toContent(): Content = Content.User(part = Part.Text(text))
    }

    data class Tool(val name: String, val result: String) : ChatMessageUiState {
        override fun toContent(): Content = Content.Tool(name = name, result = result)
    }

    data class AI(val text: String) : ChatMessageUiState {
        override fun toContent(): Content = Content.AI(part = Part.Text(text))
    }

    companion object {
        fun from(content: Content): ChatMessageUiState {
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
