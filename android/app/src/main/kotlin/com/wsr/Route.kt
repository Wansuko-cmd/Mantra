package com.wsr

import kotlinx.serialization.Serializable

internal sealed interface Route {
    @Serializable
    sealed interface Memo {
        @Serializable
        data object Index : Memo

        @Serializable
        data class Show(private val _memoId: String) : Memo {
            val memoId get() = MemoResponseId(_memoId)

            companion object {
                fun create(memoId: MemoResponseId) = Show(memoId.value)
            }
        }
    }
}