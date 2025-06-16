package com.wsr.memo.index

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.wsr.MemoController
import com.wsr.MemoResponse
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberMemoIndexPresenter(
    controller: MemoController = koinInject(),
): MemoIndexPresenter {
    val presenter = rememberRetained {
        MemoIndexPresenter(controller = controller)
    }
    LaunchedEffect(presenter) {
        presenter.initialize()
    }
    return presenter
}

internal class MemoIndexPresenter(
    private val controller: MemoController,
) : Presenter<MemoIndexUiState, UiEvent>(initialValue = MemoIndexUiState()) {
    fun initialize() {
        controller.getAll()
            .onEach { uiState = MemoIndexUiState(it) }
            .launchIn(scope)
    }

    fun onClickFabButton() {
        uiState = uiState.copy(createDialogUiState = MemoIndexCreateDialogUiState())
    }

    fun onDismissCreateDialog() {
        uiState = uiState.copy(createDialogUiState = null)
    }

    fun onChangeCreateDialogTitle(title: String) {
        val newUiState = uiState.createDialogUiState?.copy(title = title)
        uiState = uiState.copy(createDialogUiState = newUiState)
    }

    fun onChangeCreateDialogDescription(description: String) {
        val newUiState = uiState.createDialogUiState?.copy(description = description)
        uiState = uiState.copy(createDialogUiState = newUiState)
    }

    fun onClickCreateDialogPrimaryButton() {
        val (title, description) = uiState.createDialogUiState ?: return
        scope.launch {
            controller.create(title = title, description = description)
        }
    }
}

internal data class MemoIndexUiState(
    val memos: List<MemoResponse> = emptyList(),
    val createDialogUiState: MemoIndexCreateDialogUiState? = null,
) : UiState

internal data class MemoIndexCreateDialogUiState(
    val title: String = "",
    val description: String = "",
)
