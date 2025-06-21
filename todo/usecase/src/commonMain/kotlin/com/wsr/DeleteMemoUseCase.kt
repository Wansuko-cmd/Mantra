package com.wsr

class DeleteMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(id: MemoId) = id.also { repository.delete(it) }
}
