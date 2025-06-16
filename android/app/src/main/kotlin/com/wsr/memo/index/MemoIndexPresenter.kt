package com.wsr.memo.index

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.wsr.MemoController
import com.wsr.MemoResponse
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
        uiState = uiState.copy(createDialog = MemoIndexCreateDialogUiState())
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
            detailBottomSheet = MemoIndexDetailBottomSheetUiState(
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

internal data class MemoIndexUiState(
    val memos: List<MemoResponse> = emptyList(),
    val createDialog: MemoIndexCreateDialogUiState? = null,
    val detailBottomSheet: MemoIndexDetailBottomSheetUiState? = null,
) : UiState

internal data class MemoIndexCreateDialogUiState(
    val title: String = "",
    val description: String = "",
)

internal data class MemoIndexDetailBottomSheetUiState(
    val id: MemoResponseId,
    val title: String,
    val description: String,
)
