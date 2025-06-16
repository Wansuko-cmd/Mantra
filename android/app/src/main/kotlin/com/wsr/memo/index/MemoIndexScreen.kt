package com.wsr.memo.index

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wsr.MemoResponseId

@Composable
internal fun MemoIndexRoute(navigateToShow: (MemoResponseId) -> Unit) {
    Button(onClick = { navigateToShow(MemoResponseId("Sample")) }) {
        Text("Sample")
    }
}
