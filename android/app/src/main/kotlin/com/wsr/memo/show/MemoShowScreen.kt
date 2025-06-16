package com.wsr.memo.show

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wsr.MemoResponseId

@Composable
internal fun MemoShowRoute(memoId: MemoResponseId) {
    val presenter = rememberMemoShowPresenter(memoId)
    Text(memoId.value)
}
