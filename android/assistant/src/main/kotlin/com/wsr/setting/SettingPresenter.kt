package com.wsr.setting

import androidx.compose.runtime.Composable
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import com.wsr.rememberPresenter
import com.wsr.setting.store.SettingStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberSettingPresenter(store: SettingStore = koinInject()) =
    rememberPresenter { SettingPresenter(store = store) }

internal class SettingPresenter(private val store: SettingStore) :
    Presenter<SettingUiState, UiEvent>(SettingUiState()) {
    override fun onRemembered() {
        scope.launch {
            val model = store.data.first()
            uiState = uiState.copy(
                apiKey = model.apiKey,
                prompt = model.prompt,
            )
        }
    }

    fun onChangeApiKey(apiKey: String) {
        uiState = uiState.copy(apiKey = apiKey)
        scope.launch {
            store.update { it.copy(apiKey = apiKey) }
        }
    }

    fun onChangePrompt(prompt: String) {
        uiState = uiState.copy(prompt = prompt)
        scope.launch {
            store.update { it.copy(prompt = prompt) }
        }
    }
}

internal data class SettingUiState(val apiKey: String = "", val prompt: String = "") : UiState
