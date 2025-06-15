package com.wsr

class CreateMemoUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
    ) {
        val memo = Memo.create(title = title, description = description)
        repository.create(memo)
    }
}
