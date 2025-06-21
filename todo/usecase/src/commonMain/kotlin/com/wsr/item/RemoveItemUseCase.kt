package com.wsr.item

import com.wsr.ItemId
import com.wsr.Memo
import com.wsr.MemoId
import com.wsr.MemoRepository
import kotlinx.coroutines.flow.first

class RemoveItemUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        memoId: MemoId,
        itemId: ItemId,
    ): Memo {
        val memo = repository.get(memoId).first()
        return memo
            .removeItem(itemId)
            .also { repository.update(it) }
    }
}
