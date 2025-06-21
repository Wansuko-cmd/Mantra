package com.wsr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wsr.chat.ChatScreen
import com.wsr.setting.SettingScreen

@Composable
fun AssistantScreen() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = AssistantRoute.Chat,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable<AssistantRoute.Chat> {
            ChatScreen(navigateToSetting = { controller.navigate(AssistantRoute.Setting) })
        }
        composable<AssistantRoute.Setting> {
            SettingScreen()
        }
    }

}
