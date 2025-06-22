package com.wsr.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.chat.ai.PromptInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatTemplateBottomSheet(
    uiState: ChatTemplateBottomSheetUiState,
    listener: ChatTemplateBottomSheetListener,
) {
    ModalBottomSheet(onDismissRequest = listener.onDismiss) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 16.dp),
        ) {
            itemsIndexed(uiState.infos) { index, info ->
                ChatInfoCard(
                    info = info,
                    modifier = Modifier
                        .clickable { listener.onClickInfo(info) }
                        .padding(horizontal = 32.dp),
                )
                if (index != uiState.infos.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun ChatInfoCard(info: PromptInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Text(
            text = info.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = info.description.orEmpty(),
            fontSize = 16.sp,
        )
    }
}

internal data class ChatTemplateBottomSheetListener(
    val onDismiss: () -> Unit,
    val onClickInfo: (info: PromptInfo) -> Unit,
)
