package com.wsr.memo.index

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wsr.MemoResponse
import com.wsr.MemoResponseId
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun MemoIndexRoute(navigateToShow: (MemoResponseId) -> Unit) {
    val presenter = rememberMemoIndexPresenter()
    MemoIndexScreen(
        uiState = presenter.uiState,
        onClickFabButton = presenter::onClickFabButton,
        onClickMemo = navigateToShow,
        onClickMemoDetail = presenter::onClickMemoDetail,
        onClickMemoDelete = presenter::onClickMemoDelete,
        createDialogListener = MemoIndexCreateDialogListener(
            onDismiss = presenter::onDismissCreateDialog,
            onChangeTitle = presenter::onChangeCreateDialogTitle,
            onChangeDescription = presenter::onChangeCreateDialogDescription,
            onClickPrimaryButton = presenter::onClickCreateDialogPrimaryButton,
        ),
        detailBottomSheetListener = MemoIndexDetailBottomSheetListener(
            onDismiss = presenter::onDismissDetailBottomSheet,
            onChangeTitle = presenter::onChangeDetailBottomSheetTitle,
            onChangeDescription = presenter::onChangeDetailBottomSheetDescription,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoIndexScreen(
    uiState: MemoIndexUiState,
    onClickFabButton: () -> Unit,
    onClickMemo: (memoId: MemoResponseId) -> Unit,
    onClickMemoDetail: (memoId: MemoResponseId) -> Unit,
    onClickMemoDelete: (memoId: MemoResponseId) -> Unit,
    createDialogListener: MemoIndexCreateDialogListener,
    detailBottomSheetListener: MemoIndexDetailBottomSheetListener,
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
                MemoRow(
                    memo = memo,
                    onClick = { onClickMemo(memo.id) },
                    onClickDetail = { onClickMemoDetail(memo.id) },
                    onClickDelete = { onClickMemoDelete(memo.id) },
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }

    val createDialogUiState = uiState.createDialog
    if (createDialogUiState != null) {
        MemoIndexCreateDialog(
            uiState = createDialogUiState,
            listener = createDialogListener,
        )
    }

    val detailBottomSheetUiState = uiState.detailBottomSheet
    if (detailBottomSheetUiState != null) {
        MemoIndexDetailBottomSheet(
            uiState = detailBottomSheetUiState,
            listener = detailBottomSheetListener,
        )
    }
}

@Composable
private fun MemoRow(
    memo: MemoResponse,
    onClick: () -> Unit,
    onClickDetail: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(
                color = MantraTheme.colors.FieldBeige10,
                shape = MantraTheme.shape.Small,
            )
            .border(
                width = 1.dp,
                color = MantraTheme.colors.Black30,
                shape = MantraTheme.shape.Small,
            )
            .padding(12.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = memo.title,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null,
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(text = "詳細") },
                    onClick = onClickDetail,
                )
                DropdownMenuItem(
                    text = { Text(text = "削除する", color = MantraTheme.colors.Red80) },
                    onClick = onClickDelete,
                )
            }
        }
    }
}
