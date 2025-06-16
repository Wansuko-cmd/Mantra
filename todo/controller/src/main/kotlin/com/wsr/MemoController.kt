package com.wsr

import com.wsr.item.AddItemUseCase
import com.wsr.item.RemoveItemUseCase
import com.wsr.item.UpdateItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoController(private val productPath: (name: String) -> String) {
    private val memoStore = MemoStore(producePath = { productPath("memo") })
    private val repository = MemoRepositoryImpl(memoStore)

    fun get(id: MemoResponseId): Flow<MemoResponse> = GetMemoUseCase(repository)
        .invoke(id = MemoId(id.value))
        .map { memo -> MemoResponse.from(memo) }

    fun getAll(): Flow<List<MemoResponse>> = GetAllMemosUseCase(repository)
        .invoke()
        .map { memos -> memos.map { MemoResponse.from(it) } }

    suspend fun create(title: String, description: String) {
        CreateMemoUseCase(repository)
            .invoke(title = title, description = description)
    }

    suspend fun update(
        id: MemoResponseId,
        title: String,
        description: String,
    ) {
        UpdateMemoUseCase(repository)
            .invoke(
                id = MemoId(id.value),
                title = title,
                description = description,
            )
    }

    suspend fun addItem(
        id: MemoResponseId,
        title: String,
        description: String,
    ) {
        AddItemUseCase(repository)
            .invoke(
                id = MemoId(id.value),
                title = title,
                description = description,
            )
    }

    suspend fun updateItem(
        memoId: MemoResponseId,
        itemId: ItemResponseId,
        title: String? = null,
        description: String? = null,
        checked: Boolean? = null,
    ) {
        UpdateItemUseCase(repository)
            .invoke(
                memoId = MemoId(memoId.value),
                itemId = ItemId(itemId.value),
                title = title,
                description = description,
                checked = checked,
            )
    }

    suspend fun removeItem(
        memoId: MemoResponseId,
        itemId: ItemResponseId,
    ) {
        RemoveItemUseCase(repository)
            .invoke(
                memoId = MemoId(memoId.value),
                itemId = ItemId(itemId.value),
            )
    }
}
