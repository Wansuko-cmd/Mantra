package com.wsr.index

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoIndexCreateDialog(
    uiState: MemoIndexCreateDialogUiState,
    listener: MemoIndexCreateDialogListener,
) {
    BasicAlertDialog(onDismissRequest = listener.onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MantraTheme.shape.Medium,
            color = MantraTheme.colors.White100,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.title,
                    label = { Text("タイトル") },
                    onValueChange = listener.onChangeTitle,
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = uiState.description,
                    label = { Text("説明") },
                    onValueChange = listener.onChangeDescription,
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = listener.onClickPrimaryButton,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text("作成")
                }
            }
        }
    }
}

internal data class MemoIndexCreateDialogListener(
    val onDismiss: () -> Unit,
    val onChangeTitle: (String) -> Unit,
    val onChangeDescription: (String) -> Unit,
    val onClickPrimaryButton: () -> Unit,
)

@Preview
@Composable
private fun MemoIndexCreateDialogPreview() {
    MantraTheme {
        MemoIndexCreateDialog(
            uiState = MemoIndexCreateDialogUiState(
                title = "title",
                description = "description",
            ),
            listener = MemoIndexCreateDialogListener(
                onDismiss = {},
                onChangeTitle = {},
                onChangeDescription = {},
                onClickPrimaryButton = {},
            ),
        )
    }
}
