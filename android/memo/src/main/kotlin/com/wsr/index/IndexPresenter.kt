package com.wsr.index

import androidx.compose.runtime.Composable
import com.wsr.MemoController
import com.wsr.MemoResponse
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
internal fun rememberIndexPresenter(controller: MemoController = koinInject()): IndexPresenter =
    rememberPresenter {
        IndexPresenter(controller = controller)
    }

internal class IndexPresenter(
    private val controller: MemoController,
) : Presenter<IndexUiState, UiEvent>(initialValue = IndexUiState()) {
    override fun onRemembered() {
        controller.getAll()
            .onEach { uiState = IndexUiState(it) }
            .launchIn(scope)
    }

    fun onClickFabButton() {
        uiState = uiState.copy(createDialog = IndexCreateDialogUiState())
    }

    fun onDismissCreateDialog() {
        uiState = uiState.copy(createDialog = null)
    }

    fun onChangeCreateDialogTitle(title: String) {
        val newUiState = uiState.createDialog?.copy(title = title)
        uiState = uiState.copy(createDialog = newUiState)
    }

    fun onChangeCreateDialogDescription(description: String) {
        val newUiState = uiState.createDialog?.copy(description = description)
        uiState = uiState.copy(createDialog = newUiState)
    }

    fun onClickCreateDialogPrimaryButton() {
        val (title, description) = uiState.createDialog ?: return
        scope.launch {
            controller.create(title = title, description = description)
        }
    }

    fun onClickMemoDetail(id: MemoResponseId) {
        val memo = uiState.memos.firstOrNull { it.id == id } ?: return
        uiState = uiState.copy(
            detailBottomSheet = IndexDetailBottomSheetUiState(
                id = id,
                title = memo.title,
                description = memo.description,
            ),
        )
    }

    fun onClickMemoDelete(id: MemoResponseId) {
        scope.launch {
            controller.delete(id = id)
        }
    }

    fun onDismissDetailBottomSheet() {
        val detailBottomSheet = uiState.detailBottomSheet
        if (detailBottomSheet != null) {
            scope.launch {
                controller.update(
                    id = detailBottomSheet.id,
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

internal data class IndexUiState(
    val memos: List<MemoResponse> = emptyList(),
    val createDialog: IndexCreateDialogUiState? = null,
    val detailBottomSheet: IndexDetailBottomSheetUiState? = null,
) : UiState

internal data class IndexCreateDialogUiState(
    val title: String = "",
    val description: String = "",
)

internal data class IndexDetailBottomSheetUiState(
    val id: MemoResponseId,
    val title: String,
    val description: String,
)
