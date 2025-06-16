package com.wsr.memo.show

import androidx.compose.runtime.Composable
import com.wsr.ItemResponse
import com.wsr.MemoController
import com.wsr.MemoResponseId
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import io.github.takahirom.rin.rememberRetained
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
    return presenter
}

internal class MemoShowPresenter(
    private val memoId: MemoResponseId,
    private val controller: MemoController,
) : Presenter<MemoShowUiState, UiEvent>(MemoShowUiState()) {
}

internal data class MemoShowUiState(
    val items: List<ItemResponse> = emptyList(),
) : UiState
