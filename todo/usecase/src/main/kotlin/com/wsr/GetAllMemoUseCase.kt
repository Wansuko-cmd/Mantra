package com.wsr

import kotlinx.coroutines.flow.Flow

class GetAllMemoUseCase(private val repository: MemoRepository) {
    operator fun invoke(): Flow<List<Memo>> = repository.getAll()
}
