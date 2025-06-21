package com.wsr.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShowItemCard(
    item: ItemResponse,
    listener: ShowItemCardListener,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (item.checked) {
                    MantraTheme.colors.White100
                } else {
                    MantraTheme.colors.FieldBeige10
                },
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
            onCheckedChange = { listener.onChangeChecked(item.id, it) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = item.title,
            onValueChange = { listener.onChangeTitle(item.id, it) },
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
                        listener.onClickDetail(item.id)
                        expanded = false
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = "削除する", color = MantraTheme.colors.Red80) },
                    onClick = {
                        listener.onClickDelete(item.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

internal data class ShowItemCardListener(
    val onChangeTitle: (id: ItemResponseId, title: String) -> Unit,
    val onChangeChecked: (id: ItemResponseId, checked: Boolean) -> Unit,
    val onClickDetail: (id: ItemResponseId) -> Unit,
    val onClickDelete: (id: ItemResponseId) -> Unit,
)
