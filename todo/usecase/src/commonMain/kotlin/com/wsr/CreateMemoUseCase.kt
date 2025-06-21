package com.wsr

class CreateMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(title: String, description: String): Memo = Memo
        .create(title = title, description = description)
        .also { repository.create(it) }
}
