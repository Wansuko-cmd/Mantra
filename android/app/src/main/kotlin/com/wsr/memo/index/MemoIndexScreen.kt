package com.wsr.memo.index

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wsr.MemoResponseId

@Composable
internal fun MemoIndexRoute(navigateToShow: (MemoResponseId) -> Unit) {
    val presenter = rememberMemoIndexPresenter()
    MemoIndexScreen(
        uiState = presenter.uiState,
        onClickFabButton = presenter::onClickFabButton,
        onClickMemo = navigateToShow,
        createDialogListener = MemoIndexCreateDialogListener(
            onDismiss = presenter::onDismissCreateDialog,
            onChangeTitle = presenter::onChangeCreateDialogTitle,
            onChangeDescription = presenter::onChangeCreateDialogDescription,
            onClickPrimaryButton = presenter::onClickCreateDialogPrimaryButton,
        ),
    )
}

@Composable
private fun MemoIndexScreen(
    uiState: MemoIndexUiState,
    onClickFabButton: () -> Unit,
    onClickMemo: (memoId: MemoResponseId) -> Unit,
    createDialogListener: MemoIndexCreateDialogListener,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFabButton) { }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(uiState.memos) { memo ->
                Text(
                    text = memo.title,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .clickable { onClickMemo(memo.id) }
                        .padding(24.dp),
                )
            }
        }
    }

    val createDialogUiState = uiState.createDialogUiState
    if (createDialogUiState != null) {
        MemoIndexCreateDialog(
            uiState = createDialogUiState,
            listener = createDialogListener,
        )
    }
}
