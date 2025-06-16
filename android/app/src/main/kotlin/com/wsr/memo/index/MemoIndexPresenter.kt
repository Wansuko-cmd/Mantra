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
}

internal data class MemoIndexUiState(
    val memos: List<MemoResponse> = emptyList(),
) : UiState
