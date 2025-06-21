package com.wsr.item

import com.wsr.Item
import com.wsr.Memo
import com.wsr.MemoId
import com.wsr.MemoRepository
import kotlinx.coroutines.flow.first

class AddItemUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        id: MemoId,
        title: String,
        description: String,
    ): Memo {
        val memo = repository.get(id).first()
        return memo
            .addItem(title = title, description = description)
            .also { repository.update(it) }
    }
}