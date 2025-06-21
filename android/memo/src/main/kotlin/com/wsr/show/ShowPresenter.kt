package com.wsr.show

import androidx.compose.runtime.Composable
import com.wsr.ItemResponse
import com.wsr.ItemResponseId
import com.wsr.MemoController
import com.wsr.MemoResponseId
import com.wsr.Presenter
import com.wsr.UiEvent
import com.wsr.UiState
import com.wsr.rememberPresenter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
internal fun rememberShowPresenter(
    memoId: MemoResponseId,
    controller: MemoController = koinInject(),
): ShowPresenter = rememberPresenter {
    ShowPresenter(
        memoId = memoId,
        controller = controller,
    )
}

internal class ShowPresenter(
    private val memoId: MemoResponseId,
    private val controller: MemoController,
) : Presenter<ShowUiState, UiEvent>(ShowUiState()) {
    override fun onRemembered() {
        super.onRemembered()
        controller.get(memoId)
            .onEach { memo ->
                uiState = ShowUiState(
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

    fun onClickItemDetail(id: ItemResponseId) {
        val item = uiState.items.firstOrNull { it.id == id } ?: return
        uiState = uiState.copy(
            detailBottomSheet = ShowDetailBottomSheetUiState(
                id = id,
                title = item.title,
                description = item.description,
            ),
        )
    }

    fun onClickItemDelete(id: ItemResponseId) {
        scope.launch {
            controller.removeItem(
                memoId = memoId,
                itemId = id,
            )
        }
    }

    fun onDismissDetailBottomSheet() {
        val detailBottomSheet = uiState.detailBottomSheet
        if (detailBottomSheet != null) {
            scope.launch {
                controller.updateItem(
                    memoId = memoId,
                    itemId = detailBottomSheet.id,
                    title = detailBottomSheet.title,
                    description = detailBottomSheet.description,
                )
            }
            uiState = uiState.copy(detailBottomSheet = null)
        }
    }

    fun onChangeDetailBottomSheetTitle(title: String) {
        val newUiState = uiState.detailBottomSheet?.copy(title = title)
        uiState = uiState.copy(detailBottomSheet = newUiState)
    }

    fun onChangeDetailBottomSheetDescription(description: String) {
        val newUiState = uiState.detailBottomSheet?.copy(description = description)
        uiState = uiState.copy(detailBottomSheet = newUiState)
    }
}

internal data class ShowUiState(
    val title: String = "",
    val items: List<ItemResponse> = emptyList(),
    val detailBottomSheet: ShowDetailBottomSheetUiState? = null,
) : UiState {
    fun updateItem(id: ItemResponseId, block: (ItemResponse) -> ItemResponse) =
        copy(items = items.map { if (it.id == id) block(it) else it }.sortedBy { it.checked })
}

internal data class ShowDetailBottomSheetUiState(
    val id: ItemResponseId,
    val title: String,
    val description: String,
)
