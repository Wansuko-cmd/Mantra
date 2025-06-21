package com.wsr.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun UserBubble(message: ChatMessageUiState.User, modifier: Modifier = Modifier) {
    Bubble(
        text = message.text,
        color = MantraTheme.colors.PrimaryGreen20,
        modifier = modifier,
    )
}

@Composable
internal fun ToolBubble(message: ChatMessageUiState.Tool, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        Bubble(
            text = "${message.name}を使用",
            color = MantraTheme.colors.FieldBeige20,
            modifier = Modifier.clickable { expanded = true },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = message.result) },
                onClick = { expanded = false },
            )
        }
    }
}

@Composable
internal fun AIBubble(message: ChatMessageUiState.AI, modifier: Modifier = Modifier) {
    Bubble(
        text = message.text,
        color = MantraTheme.colors.Black20,
        modifier = modifier,
    )
}

@Composable
private fun Bubble(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = color, shape = MantraTheme.shape.Medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .widthIn(max = 250.dp),
    ) {
        Text(text = text)
    }
}
