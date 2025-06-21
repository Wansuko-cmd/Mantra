package com.wsr.setting

import androidx.compose.runtime.Composable
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import com.wsr.rememberPresenter

@Composable
internal fun rememberSettingPresenter() = rememberPresenter { SettingPresenter() }

internal class SettingPresenter : Presenter<SettingUiState, UiEvent>(SettingUiState()) {
    fun onChangeApiKey(apiKey: String) {
        uiState = uiState.copy(apiKey = apiKey)
    }

    fun onChangePrompt(prompt: String) {
        uiState = uiState.copy(prompt = prompt)
    }
}

internal data class SettingUiState(
    val apiKey: String = "",
    val prompt: String = "",
) : UiState
