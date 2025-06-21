package com.wsr.item

import com.wsr.ItemId
import com.wsr.Memo
import com.wsr.MemoId
import com.wsr.MemoRepository
import kotlinx.coroutines.flow.first

class MoveItemUseCase(private val repository: MemoRepository) {
    suspend operator fun invoke(
        itemId: ItemId,
        from: MemoId,
        to: MemoId,
    ): Memo {
        val fromMemo = repository.get(from).first()
        val toMemo = repository.get(to).first()

        val item = fromMemo.getItem(itemId)

        fromMemo
            .removeItem(id = item.id)
            .also { repository.update(it) }
        return toMemo
            .addItem(
                title = item.title,
                description = item.description,
                checked = item.checked,
            )
            .also { repository.update(it) }
    }
}