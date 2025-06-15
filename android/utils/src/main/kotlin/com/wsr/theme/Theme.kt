package com.wsr.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MantraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MantraTheme.colorScheme,
        content = content,
    )
}

object MantraTheme
