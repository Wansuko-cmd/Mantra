package com.wsr.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wsr.ItemResponse
import com.wsr.ItemResponseId
import com.wsr.MemoResponseId
import com.wsr.theme.MantraTheme

@Composable
internal fun ShowScreen(memoId: MemoResponseId, onBackPress: () -> Unit) {
    val presenter = rememberShowPresenter(memoId)
    ShowScreen(
        uiState = presenter.uiState,
        onBackPress = onBackPress,
        onClickFabButton = presenter::onClickFabButton,
        itemCardListener = ShowItemCardListener(
            onChangeTitle = presenter::onChangeItemTitle,
            onChangeChecked = presenter::onChangeItemChecked,
            onClickDetail = presenter::onClickItemDetail,
            onClickDelete = presenter::onClickItemDelete,
        ),
        detailBottomSheetListener = ShowDetailBottomSheetListener(
            onDismiss = presenter::onDismissDetailBottomSheet,
            onChangeTitle = presenter::onChangeDetailBottomSheetTitle,
            onChangeDescription = presenter::onChangeDetailBottomSheetDescription,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowScreen(
    uiState: ShowUiState,
    onBackPress: () -> Unit,
    onClickFabButton: () -> Unit,
    itemCardListener: ShowItemCardListener,
    detailBottomSheetListener: ShowDetailBottomSheetListener,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFabButton) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            items(uiState.items, key = { it.id.value }) { item ->
                ShowItemCard(
                    item = item,
                    listener = itemCardListener,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .animateItem(),
                )
            }
        }
    }

    val detailBottomSheetUiState = uiState.detailBottomSheet
    if (detailBottomSheetUiState != null) {
        ShowDetailBottomSheet(
            uiState = detailBottomSheetUiState,
            listener = detailBottomSheetListener,
        )
    }
}

@Preview
@Composable
private fun ShowScreenPreview() {
    MantraTheme {
        ShowScreen(
            uiState = ShowUiState(
                title = "メモタイトル",
                items = listOf(
                    ItemResponse(
                        id = ItemResponseId("1"),
                        title = "タイトル1",
                        description = "説明文1",
                        checked = false,
                    ),
                    ItemResponse(
                        id = ItemResponseId("2"),
                        title = "タイトル2",
                        description = "説明文2",
                        checked = false,
                    ),
                    ItemResponse(
                        id = ItemResponseId("3"),
                        title = "タイトル3",
                        description = "説明文3",
                        checked = true,
                    ),
                ),
            ),
            onBackPress = {},
            onClickFabButton = {},
            itemCardListener = ShowItemCardListener(
                onClickDetail = {},
                onClickDelete = {},
                onChangeTitle = { _, _ -> },
                onChangeChecked = { _, _ -> },
            ),
            detailBottomSheetListener = ShowDetailBottomSheetListener(
                onDismiss = {},
                onChangeTitle = {},
                onChangeDescription = {},
            ),
        )
    }
}
