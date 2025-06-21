package com.wsr.index

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
internal fun IndexMemoCard(
    memo: MemoResponse,
    listener: IndexMemoCardListener,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .clickable(onClick = { listener.onClick(memo.id) })
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
                    onClick = {
                        listener.onClickDetail(memo.id)
                        expanded = false
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = "削除する", color = MantraTheme.colors.Red80) },
                    onClick = {
                        listener.onClickDelete(memo.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

internal data class IndexMemoCardListener(
    val onClick: (id: MemoResponseId) -> Unit,
    val onClickDetail: (id: MemoResponseId) -> Unit,
    val onClickDelete: (id: MemoResponseId) -> Unit,
)
