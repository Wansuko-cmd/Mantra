package com.wsr.item

import com.wsr.MemoId
import com.wsr.MemoRepository
import kotlinx.coroutines.flow.first

class AddItemUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        id: MemoId,
        title: String,
        description: String,
    ) {
        val memo = repository.get(id).first()
        val newMemo = memo.addItem(title = title, description = description)
        repository.update(newMemo)
    }
}