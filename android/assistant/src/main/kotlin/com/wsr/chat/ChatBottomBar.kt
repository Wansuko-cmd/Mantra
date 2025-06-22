package com.wsr.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun ChatBottomBar(input: String, listener: ChatBottomBarListener) {
    Row(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    strokeWidth = 1.dp.toPx(),
                    color = MantraTheme.colors.Black20,
                    start = Offset.Zero,
                    end = Offset.Zero.copy(x = size.width),
                )
            }
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = input,
            onValueChange = listener.onChangeInput,
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MantraTheme.colors.Black10,
                    shape = MantraTheme.shape.Medium,
                )
                .border(
                    width = 1.dp,
                    color = MantraTheme.colors.Black20,
                    shape = MantraTheme.shape.Medium,
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
        IconButton(onClick = listener.onClickTemplate) {
            Icon(
                imageVector = Icons.Default.PostAdd,
                tint = MantraTheme.colors.Black60,
                contentDescription = null,
            )
        }
        IconButton(
            onClick = listener.onClickSend,
            enabled = input.isNotBlank(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = MantraTheme.colors.SecondaryBeige60,
            )
        }
    }
}

internal data class ChatBottomBarListener(
    val onChangeInput: (String) -> Unit,
    val onClickTemplate: () -> Unit,
    val onClickSend: () -> Unit,
)
