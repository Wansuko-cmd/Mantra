package com.wsr

class DeleteMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(id: MemoId) {
        repository.delete(id)
    }
}
