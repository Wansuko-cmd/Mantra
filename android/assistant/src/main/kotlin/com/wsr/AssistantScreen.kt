package com.wsr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
fun AssistantScreen() {
    val presenter = rememberAssistantPresenter()
    AssistantScreen(
        uiState = presenter.uiState,
        onChangeInput = presenter::onChangeInput,
        onClickSend = presenter::onClickSend,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssistantScreen(
    uiState: AssistantUiState,
    onChangeInput: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("アシスタント面") })
        },
        bottomBar = {
            AssistantBottomBar(
                input = uiState.input,
                onChangeInput = onChangeInput,
                onClickSend = onClickSend,
            )
        },
        modifier = Modifier.navigationBarsPadding(),
    ) { innerPadding ->
        LazyColumn(
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
        ) {
            items(uiState.messages.reversed()) { message ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    when (message) {
                        is MessageUiState.User -> Bubble(
                            text = message.text,
                            color = MantraTheme.colors.PrimaryGreen20,
                            modifier = Modifier.align(Alignment.CenterEnd),
                        )

                        is MessageUiState.Tool -> Box(modifier = Modifier.align(Alignment.Center)) {
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

                        is MessageUiState.AI -> Bubble(
                            text = message.text,
                            color = MantraTheme.colors.Black20,
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistantBottomBar(
    input: String,
    onChangeInput: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = input,
            onValueChange = onChangeInput,
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .background(
                    color = MantraTheme.colors.Black20,
                    shape = MantraTheme.shape.Medium,
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
        IconButton(onClick = onClickSend) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = MantraTheme.colors.SecondaryBeige60,
            )
        }
    }
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
