package com.wsr.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TemplateTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
