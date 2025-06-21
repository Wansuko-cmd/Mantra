package com.wsr

import java.util.UUID

@ConsistentCopyVisibility
data class Memo private constructor(
    val id: MemoId,
    val title: String,
    val description: String,
    val items: List<Item>,
) {
    fun update(
        title: String,
        description: String,
    ) = copy(
        title = title,
        description = description,
    )

    fun getItem(id: ItemId) = items.first { it.id == id }

    fun addItem(
        title: String,
        description: String,
        checked: Boolean = false,
    ) = copy(
        items = items + Item(title = title, description = description, checked = checked),
    )

    fun updateItem(
        id: ItemId,
        title: String?,
        description: String?,
        checked: Boolean?,
    ) = copy(
        items = items.map { item ->
            if (item.id == id) {
                Item(
                    id = id,
                    title = title ?: item.title,
                    description = description ?: item.description,
                    checked = checked ?: item.checked,
                )
            } else {
                item
            }
        },
    )

    fun removeItem(id: ItemId) = copy(items = items.filterNot { it.id == id })

    companion object {
        fun create(
            title: String,
            description: String,
        ) = Memo(
            id = MemoId(UUID.randomUUID().toString()),
            title = title,
            description = description,
            items = emptyList(),
        )

        fun reconstruct(
            id: MemoId,
            title: String,
            description: String,
            items: List<Item>,
        ) = Memo(
            id = id,
            title = title,
            description = description,
            items = items,
        )
    }
}

@ConsistentCopyVisibility
data class Item internal constructor(
    val id: ItemId = ItemId(UUID.randomUUID().toString()),
    val title: String,
    val description: String,
    val checked: Boolean = false,
) {
    companion object {
        fun reconstruct(
            id: ItemId,
            title: String,
            description: String,
            checked: Boolean,
        ) = Item(
            id = id,
            title = title,
            description = description,
            checked = checked,
        )
    }
}

@JvmInline
value class MemoId(val value: String)

@JvmInline
value class ItemId(val value: String)
