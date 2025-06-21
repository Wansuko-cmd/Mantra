package com.wsr.memo.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.ItemResponse
import com.wsr.ItemResponseId
import com.wsr.MemoResponseId
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun MemoShowScreen(memoId: MemoResponseId) {
    val presenter = rememberMemoShowPresenter(memoId)
    MemoShowScreen(
        uiState = presenter.uiState,
        onClickFabButton = presenter::onClickFabButton,
        onChangeItemTitle = presenter::onChangeItemTitle,
        onChangeItemChecked = presenter::onChangeItemChecked,
        onClickItemDetail = presenter::onClickItemDetail,
        onClickItemDelete = presenter::onClickItemDelete,
        detailBottomSheetListener = MemoShowDetailBottomSheetListener(
            onDismiss = presenter::onDismissDetailBottomSheet,
            onChangeTitle = presenter::onChangeDetailBottomSheetTitle,
            onChangeDescription = presenter::onChangeDetailBottomSheetDescription,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoShowScreen(
    uiState: MemoShowUiState,
    onClickFabButton: () -> Unit,
    onChangeItemTitle: (id: ItemResponseId, title: String) -> Unit,
    onChangeItemChecked: (id: ItemResponseId, checked: Boolean) -> Unit,
    onClickItemDetail: (itemId: ItemResponseId) -> Unit,
    onClickItemDelete: (itemId: ItemResponseId) -> Unit,
    detailBottomSheetListener: MemoShowDetailBottomSheetListener,

) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = uiState.title) })
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            items(uiState.items) { item ->
                ItemRow(
                    item = item,
                    onChangeTitle = { onChangeItemTitle(item.id, it) },
                    onChangeChecked = { onChangeItemChecked(item.id, it) },
                    onClickDetail = { onClickItemDetail(item.id) },
                    onClickDelete = { onClickItemDelete(item.id) },
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }

    val detailBottomSheetUiState = uiState.detailBottomSheet
    if (detailBottomSheetUiState != null) {
        MemoShowDetailBottomSheet(
            uiState = detailBottomSheetUiState,
            listener = detailBottomSheetListener,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemRow(
    item: ItemResponse,
    onChangeTitle: (title: String) -> Unit,
    onChangeChecked: (checked: Boolean) -> Unit,
    onClickDetail: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = { onChangeChecked(it) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = item.title,
            onValueChange = { onChangeTitle(it) },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            textStyle = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = item.title,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    container = {},
                    contentPadding = PaddingValues(0.dp),
                )
            },
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
                    onClick = {
                        onClickDetail()
                        expanded = false
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = "削除する", color = MantraTheme.colors.Red80) },
                    onClick = {
                        onClickDelete()
                        expanded = false
                    },
                )
            }
        }
    }
}
