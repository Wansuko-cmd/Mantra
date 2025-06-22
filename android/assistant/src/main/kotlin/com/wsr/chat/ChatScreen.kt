package com.wsr.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wsr.theme.MantraTheme
import com.wsr.theme.colors

@Composable
internal fun ChatScreen(navigateToSetting: () -> Unit) {
    val presenter = rememberChatPresenter()
    ChatScreen(
        uiState = presenter.uiState,
        onClickSetting = navigateToSetting,
        bottomBarListener = ChatBottomBarListener(
            onChangeInput = presenter::onChangeInput,
            onClickTemplate = presenter::onClickTemplate,
            onClickSend = presenter::onClickSend,
        ),
        templateBottomSheetListener = ChatTemplateBottomSheetListener(
            onDismiss = presenter::onDismissTemplateBottomSheet,
            onClickInfo = presenter::onClickTemplateBottomSheetInfo,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    uiState: ChatUiState,
    onClickSetting: () -> Unit,
    bottomBarListener: ChatBottomBarListener,
    templateBottomSheetListener: ChatTemplateBottomSheetListener,
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
                },
            )
        },
        bottomBar = {
            ChatBottomBar(
                input = uiState.input,
                listener = bottomBarListener,
            )
        },
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
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

    val templateBottomSheet = uiState.templateBottomSheet
    if (templateBottomSheet != null) {
        ChatTemplateBottomSheet(
            uiState = templateBottomSheet,
            listener = templateBottomSheetListener,
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MantraTheme.colors.Black100.copy(alpha = 0.3f)),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
            )
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MantraTheme {
        ChatScreen(
            uiState = ChatUiState(
                messages = listOf(
                    ChatMessageUiState.User("Userのメッセージ"),
                    ChatMessageUiState.Tool(name = "ツール名", result = "結果"),
                    ChatMessageUiState.AI("AIのメッセージ"),
                ),
                input = "入力",
            ),
            onClickSetting = {},
            bottomBarListener = ChatBottomBarListener(
                onClickTemplate = {},
                onChangeInput = {},
                onClickSend = {},
            ),
            templateBottomSheetListener = ChatTemplateBottomSheetListener(
                onDismiss = {},
                onClickInfo = {},
            ),
        )
    }
}
