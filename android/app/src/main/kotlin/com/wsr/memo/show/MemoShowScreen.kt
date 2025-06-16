package com.wsr.memo.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wsr.MemoResponseId

@Composable
internal fun MemoShowRoute(memoId: MemoResponseId) {
    val presenter = rememberMemoShowPresenter(memoId)
    MemoShowScreen(uiState = presenter.uiState)
}

@Composable
private fun MemoShowScreen(uiState: MemoShowUiState) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(uiState.items) { item ->
                Text(item.title)
            }
        }
    }
}
