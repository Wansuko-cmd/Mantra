package com.wsr

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoRepositoryImpl(private val memoStore: MemoStore) : MemoRepository {
    override fun get(id: MemoId): Flow<Memo> = getAll()
        .map { memos -> memos.first { it.id == id } }

    override fun getAll(): Flow<List<Memo>> = memoStore.data
        .map { memos -> memos.map { it.toMemo() } }

    override suspend fun create(memo: Memo) {
        memoStore.create(memo.toModel())
    }

    override suspend fun update(memo: Memo) {
        memoStore.update(memo.toModel())
    }

    override suspend fun delete(id: MemoId) {
        memoStore.delete(id.value)
    }
}

private fun MemoModel.toMemo() = Memo.reconstruct(
    id = MemoId(id),
    title = title,
    description = description,
    items = items.map { it.toItem() },
)

private fun ItemModel.toItem() = Item.reconstruct(
    id = ItemId(id),
    title = title,
    description = description,
    checked = checked,
)

private fun Memo.toModel() = MemoModel(
    id = id.value,
    title = title,
    description = description,
    items = items.map { it.toModel() }
)

private fun Item.toModel() = ItemModel(
    id = id.value,
    title = title,
    description = description,
    checked = checked,
)
