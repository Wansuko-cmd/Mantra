package com.wsr

import kotlinx.coroutines.flow.first

class UpdateMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        id: MemoId,
        title: String,
        description: String,
    ): Memo {
        val memo = repository.get(id).first()
        return memo
            .update(title, description)
            .also { repository.update(it) }
    }
}
