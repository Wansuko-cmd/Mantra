package com.wsr.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingScreen(onBackPress: () -> Unit) {
    val presenter = rememberSettingPresenter()
    SettingScreen(
        uiState = presenter.uiState,
        onBackPress = onBackPress,
        onChangeApiKey = presenter::onChangeApiKey,
        onChangePrompt = presenter::onChangePrompt,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreen(
    uiState: SettingUiState,
    onBackPress: () -> Unit,
    onChangeApiKey: (String) -> Unit,
    onChangePrompt: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
        ) {
            Text("API Key")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = onChangeApiKey,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("System Prompt")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.prompt,
                onValueChange = onChangePrompt,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
