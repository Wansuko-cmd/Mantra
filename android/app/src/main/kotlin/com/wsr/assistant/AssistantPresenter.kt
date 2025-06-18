package com.wsr.assistant

import androidx.compose.runtime.Composable
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import io.github.takahirom.rin.rememberRetained

@Composable
internal fun rememberAssistantPresenter(): AssistantPresenter {
    val presenter = rememberRetained {
        AssistantPresenter()
    }
    return presenter
}

internal class AssistantPresenter : Presenter<AssistantUiState, UiEvent>(AssistantUiState()) {
    fun onChangeInput(input: String) {
        uiState = uiState.copy(input = input)
    }

    fun onClickSend() {
        val message = MessageUiState.User(uiState.input)
        uiState = uiState.copy(messages = uiState.messages + message)
        uiState = uiState.copy(input = "")
    }
}

internal data class AssistantUiState(
    val messages: List<MessageUiState> = emptyList(),
    val input: String = "",
) : UiState

internal sealed interface MessageUiState {
    data class User(val text: String) : MessageUiState
    data class AI(val text: String) : MessageUiState
}
