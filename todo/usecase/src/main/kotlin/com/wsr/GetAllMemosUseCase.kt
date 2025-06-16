package com.wsr

import kotlinx.coroutines.flow.Flow

class GetAllMemosUseCase(private val repository: MemoRepository) {
    operator fun invoke(): Flow<List<Memo>> = repository.getAll()
}
