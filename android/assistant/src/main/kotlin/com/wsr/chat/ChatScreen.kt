package com.wsr.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors
import com.wsr.theme.shape

@Composable
internal fun ChatScreen(navigateToSetting: () -> Unit) {
    val presenter = rememberChatPresenter()
    ChatScreen(
        uiState = presenter.uiState,
        onClickSetting = navigateToSetting,
        onChangeInput = presenter::onChangeInput,
        onClickSend = presenter::onClickSend,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    uiState: ChatUiState,
    onClickSetting: () -> Unit,
    onChangeInput: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("アシスタント面") },
                actions = {
                    IconButton(onClick = onClickSetting) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        bottomBar = {
            ChatBottomBar(
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
                        is ChatMessageUiState.User -> UserBubble(
                            message = message,
                            modifier = Modifier.align(Alignment.CenterEnd),
                        )

                        is ChatMessageUiState.Tool -> ToolBubble(
                            message = message,
                            modifier = Modifier.align(Alignment.Center),
                        )

                        is ChatMessageUiState.AI -> AIBubble(
                            message,
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBottomBar(
    input: String,
    onChangeInput: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .heightIn(min = 64.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = input,
            onValueChange = onChangeInput,
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
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
