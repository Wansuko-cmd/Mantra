package com.wsr

import com.wsr.item.AddItemUseCase
import com.wsr.item.MoveItemUseCase
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

    suspend fun create(
        title: String,
        description: String,
    ): MemoResponse {
        return CreateMemoUseCase(repository)
            .invoke(title = title, description = description)
            .let { MemoResponse.from(it) }
    }

    suspend fun update(
        id: MemoResponseId,
        title: String? = null,
        description: String? = null,
    ): MemoResponse {
        return UpdateMemoUseCase(repository)
            .invoke(
                id = MemoId(id.value),
                title = title,
                description = description,
            )
            .let { MemoResponse.from(it) }
    }

    suspend fun delete(id: MemoResponseId): MemoResponseId {
        return DeleteMemoUseCase(repository)
            .invoke(id = MemoId(id.value))
            .let { MemoResponseId(it.value) }
    }

    suspend fun addItem(
        id: MemoResponseId,
        title: String,
        description: String,
    ): MemoResponse {
        return AddItemUseCase(repository)
            .invoke(
                id = MemoId(id.value),
                title = title,
                description = description,
            )
            .let { MemoResponse.from(it) }
    }

    suspend fun updateItem(
        memoId: MemoResponseId,
        itemId: ItemResponseId,
        title: String? = null,
        description: String? = null,
        checked: Boolean? = null,
    ): MemoResponse {
        return UpdateItemUseCase(repository)
            .invoke(
                memoId = MemoId(memoId.value),
                itemId = ItemId(itemId.value),
                title = title,
                description = description,
                checked = checked,
            )
            .let { MemoResponse.from(it) }
    }

    suspend fun moveItem(
        itemId: ItemResponseId,
        from: MemoResponseId,
        to: MemoResponseId,
    ): MemoResponse {
        return MoveItemUseCase(repository)
            .invoke(
                itemId = ItemId(itemId.value),
                from = MemoId(from.value),
                to = MemoId(to.value),
            )
            .let { MemoResponse.from(it) }
    }

    suspend fun removeItem(
        memoId: MemoResponseId,
        itemId: ItemResponseId,
    ): MemoResponse {
        return RemoveItemUseCase(repository)
            .invoke(
                memoId = MemoId(memoId.value),
                itemId = ItemId(itemId.value),
            )
            .let { MemoResponse.from(it) }
    }
}
