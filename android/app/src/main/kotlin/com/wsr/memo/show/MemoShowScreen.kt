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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.ItemResponseId
import com.wsr.MemoResponseId
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun MemoShowRoute(memoId: MemoResponseId) {
    val presenter = rememberMemoShowPresenter(memoId)
    MemoShowScreen(
        uiState = presenter.uiState,
        onClickFabButton = presenter::onClickFabButton,
        onChangeItemTitle = presenter::onChangeItemTitle,
        onChangeItemChecked = presenter::onChangeItemChecked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoShowScreen(
    uiState: MemoShowUiState,
    onClickFabButton: () -> Unit,
    onChangeItemTitle: (id: ItemResponseId, title: String) -> Unit,
    onChangeItemChecked: (id: ItemResponseId, checked: Boolean) -> Unit,
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
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
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
                        onCheckedChange = { onChangeItemChecked(item.id, it) },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = item.title,
                        onValueChange = { onChangeItemTitle(item.id, it) },
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
                }
            }
        }
    }
}
