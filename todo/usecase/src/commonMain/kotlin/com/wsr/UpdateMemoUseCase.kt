package com.wsr

import kotlinx.coroutines.flow.first

class UpdateMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        id: MemoId,
        title: String? = null,
        description: String? = null,
    ): Memo {
        val memo = repository.get(id).first()
        return memo
            .update(title = title, description = description)
            .also { repository.update(it) }
    }
}
