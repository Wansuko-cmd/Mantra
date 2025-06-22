package com.wsr.chat

import androidx.compose.runtime.Composable
import com.wsr.MemoController
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import com.wsr.chat.ai.Assistant
import com.wsr.chat.ai.Content
import com.wsr.chat.ai.Part
import com.wsr.chat.ai.PromptInfo
import com.wsr.chat.ai.gemini.GeminiAssistant
import com.wsr.rememberPresenter
import com.wsr.setting.store.SettingStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberChatPresenter(
    controller: MemoController = koinInject(),
    store: SettingStore = koinInject(),
): ChatPresenter = rememberPresenter {
    ChatPresenter(controller = controller, store = store)
}

internal class ChatPresenter(
    private val controller: MemoController,
    private val store: SettingStore,
) : Presenter<ChatUiState, UiEvent>(ChatUiState()) {
    fun onChangeInput(input: String) {
        uiState = uiState.copy(input = input)
    }

    fun onClickSend() {
        if (uiState.isLoading) return
        val message = uiState.input
        val history = uiState.messages
        scope.launch {
            uiState = uiState.copy(isLoading = true)
            createAssistant().send(
                message = message,
                history = history.map { it.toContent() },
            ).collect { messages ->
                uiState = uiState.copy(messages = messages.map { ChatMessageUiState.from(it) })
            }
            uiState = uiState.copy(isLoading = false)
        }
    }

    fun onClickTemplate() {
        scope.launch {
            val infos = createAssistant().promptInfos
            uiState = uiState.copy(
                templateBottomSheet = ChatTemplateBottomSheetUiState(infos = infos),
            )
        }
    }

    fun onDismissTemplateBottomSheet() {
        uiState = uiState.copy(templateBottomSheet = null)
    }

    fun onClickTemplateBottomSheetInfo(info: PromptInfo, args: Map<String, String>? = null) {
        if (uiState.isLoading) return
        scope.launch {
            uiState = uiState.copy(templateBottomSheet = null, isLoading = true)
            createAssistant()
                .sendPrompt(info.name, args)
                .collect { messages ->
                    uiState = uiState.copy(messages = messages.map { ChatMessageUiState.from(it) })
                }
            uiState = uiState.copy(isLoading = false)
        }
    }

    private suspend fun createAssistant(): Assistant {
        val model = store.data.first()
        return GeminiAssistant.create(
            controller = controller,
            apiKey = model.apiKey,
            prompt = model.prompt,
        )
    }
}

internal data class ChatUiState(
    val messages: List<ChatMessageUiState> = emptyList(),
    val input: String = "",
    val templateBottomSheet: ChatTemplateBottomSheetUiState? = null,
    val isLoading: Boolean = false,
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

internal data class ChatTemplateBottomSheetUiState(
    val infos: List<PromptInfo>,
)
