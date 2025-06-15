package com.wsr

import kotlinx.coroutines.flow.first

class UpdateMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        id: MemoId,
        title: String,
        description: String,
    ) {
        val memo = repository.get(id).first()
        val newMemo = memo.update(title, description)
        repository.create(newMemo)
    }
}
