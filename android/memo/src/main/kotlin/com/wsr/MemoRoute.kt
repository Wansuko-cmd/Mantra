package com.wsr

import kotlinx.serialization.Serializable

internal sealed interface MemoRoute {
    @Serializable
    data object Index : MemoRoute

    @Serializable
    data class Show(private val _memoId: String) : MemoRoute {
        val memoId get() = MemoResponseId(_memoId)

        companion object {
            fun create(memoId: MemoResponseId) = Show(memoId.value)
        }
    }
}
