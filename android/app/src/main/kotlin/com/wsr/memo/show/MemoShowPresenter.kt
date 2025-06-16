package com.wsr.memo.show

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.wsr.ItemResponse
import com.wsr.MemoController
import com.wsr.MemoResponseId
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberMemoShowPresenter(
    memoId: MemoResponseId,
    controller: MemoController = koinInject(),
): MemoShowPresenter {
    val presenter = rememberRetained {
        MemoShowPresenter(
            memoId = memoId,
            controller = controller,
        )
    }
    LaunchedEffect(presenter) {
        presenter.initialize()
    }
    return presenter
}

internal class MemoShowPresenter(
    private val memoId: MemoResponseId,
    private val controller: MemoController,
) : Presenter<MemoShowUiState, UiEvent>(MemoShowUiState()) {
    fun initialize() {
        scope.launch {
            val memo = controller.get(memoId).first()
            uiState = MemoShowUiState(items = memo.items)
        }
    }
}

internal data class MemoShowUiState(
    val items: List<ItemResponse> = emptyList(),
) : UiState
