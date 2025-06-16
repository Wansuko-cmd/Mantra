package com.wsr.memo.show

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.wsr.ItemResponse
import com.wsr.ItemResponseId
import com.wsr.MemoController
import com.wsr.MemoResponseId
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        controller.get(memoId)
            .onEach { memo ->
                uiState = MemoShowUiState(
                    title = memo.title,
                    items = memo.items,
                )
            }
            .launchIn(scope)
    }

    fun onClickFabButton() {
        scope.launch {
            controller.addItem(
                id = memoId,
                title = "",
                description = "",
            )
        }
    }

    fun onChangeItemTitle(id: ItemResponseId, title: String) {
        uiState = uiState.updateItem(id) { it.copy(title = title) }
        scope.launch {
            controller.updateItem(
                memoId = memoId,
                itemId = id,
                title = title,
            )
        }
    }

    fun onChangeItemChecked(id: ItemResponseId, checked: Boolean) {
        uiState = uiState.updateItem(id) { it.copy(checked = checked) }
        scope.launch {
            controller.updateItem(
                memoId = memoId,
                itemId = id,
                checked = checked,
            )
        }
    }
}

internal data class MemoShowUiState(
    val title: String = "",
    val items: List<ItemResponse> = emptyList(),
) : UiState {
    fun updateItem(id: ItemResponseId, block: (ItemResponse) -> ItemResponse) =
        copy(items = items.map { if (it.id == id) block(it) else it })
}
