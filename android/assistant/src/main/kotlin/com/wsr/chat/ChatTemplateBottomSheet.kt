package com.wsr.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                    onClickButton = { listener.onClickInfo(info) },
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
                if (index != uiState.infos.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun ChatInfoCard(
    info: PromptInfo,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clickable { expanded = !expanded }
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
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onClickButton,
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                Text("使用する")
            }
        }
    }
}

internal data class ChatTemplateBottomSheetListener(
    val onDismiss: () -> Unit,
    val onClickInfo: (info: PromptInfo) -> Unit,
)
