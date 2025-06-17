package com.wsr

import kotlinx.coroutines.flow.Flow

class GetMemoUseCase(private val repository: MemoRepository) {
    operator fun invoke(id: MemoId): Flow<Memo> = repository.get(id)
}
