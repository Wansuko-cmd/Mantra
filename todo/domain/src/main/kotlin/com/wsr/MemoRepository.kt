package com.wsr

import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    fun get(id: MemoId): Flow<Memo>
    fun getAll(): Flow<List<Memo>>
    suspend fun create(memo: Memo)
    suspend fun update(memo: Memo)
}
