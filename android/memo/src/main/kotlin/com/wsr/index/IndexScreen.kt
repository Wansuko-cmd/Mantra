package com.wsr.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wsr.MemoResponseId
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors

@Composable
internal fun IndexScreen(navigateToShow: (MemoResponseId) -> Unit) {
    val presenter = rememberIndexPresenter()
    IndexScreen(
        uiState = presenter.uiState,
        onClickFabButton = presenter::onClickFabButton,
        memoCardListener = IndexMemoCardListener(
            onClick = navigateToShow,
            onClickDetail = presenter::onClickMemoDetail,
            onClickDelete = presenter::onClickMemoDelete,
        ),
        createDialogListener = IndexCreateDialogListener(
            onDismiss = presenter::onDismissCreateDialog,
            onChangeTitle = presenter::onChangeCreateDialogTitle,
            onChangeDescription = presenter::onChangeCreateDialogDescription,
            onClickPrimaryButton = presenter::onClickCreateDialogPrimaryButton,
        ),
        detailBottomSheetListener = IndexDetailBottomSheetListener(
            onDismiss = presenter::onDismissDetailBottomSheet,
            onChangeTitle = presenter::onChangeDetailBottomSheetTitle,
            onChangeDescription = presenter::onChangeDetailBottomSheetDescription,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IndexScreen(
    uiState: IndexUiState,
    onClickFabButton: () -> Unit,
    memoCardListener: IndexMemoCardListener,
    createDialogListener: IndexCreateDialogListener,
    detailBottomSheetListener: IndexDetailBottomSheetListener,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "メモ一覧") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFabButton) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MantraTheme.colors.White100)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            items(uiState.memos) { memo ->
                IndexMemoCard(
                    memo = memo,
                    listener = memoCardListener,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }

    val createDialog = uiState.createDialog
    if (createDialog != null) {
        IndexCreateDialog(
            uiState = createDialog,
            listener = createDialogListener,
        )
    }

    val detailBottomSheet = uiState.detailBottomSheet
    if (detailBottomSheet != null) {
        IndexDetailBottomSheet(
            uiState = detailBottomSheet,
            listener = detailBottomSheetListener,
        )
    }
}
