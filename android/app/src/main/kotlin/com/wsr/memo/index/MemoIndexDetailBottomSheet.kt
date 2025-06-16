package com.wsr.memo.index

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoIndexDetailBottomSheet(
    uiState: MemoIndexDetailBottomSheetUiState,
    listener: MemoIndexDetailBottomSheetListener,
) {
    ModalBottomSheet(onDismissRequest = listener.onDismiss) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)) {
            OutlinedTextField(
                value = uiState.title,
                label = { Text("タイトル") },
                onValueChange = listener.onChangeTitle,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.description,
                label = { Text("詳細") },
                onValueChange = listener.onChangeDescription,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

data class MemoIndexDetailBottomSheetListener(
    val onDismiss: () -> Unit,
    val onChangeTitle: (String) -> Unit,
    val onChangeDescription: (String) -> Unit,
)
