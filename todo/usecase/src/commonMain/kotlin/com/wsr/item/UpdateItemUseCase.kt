package com.wsr.item

import com.wsr.ItemId
import com.wsr.Memo
import com.wsr.MemoId
import com.wsr.MemoRepository
import kotlinx.coroutines.flow.first

class UpdateItemUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        memoId: MemoId,
        itemId: ItemId,
        title: String?,
        description: String?,
        checked: Boolean?,
    ): Memo {
        val memo = repository.get(memoId).first()
        return memo
            .updateItem(
                id = itemId,
                title = title,
                description = description,
                checked = checked,
            )
            .also { repository.update(it) }
    }
}
