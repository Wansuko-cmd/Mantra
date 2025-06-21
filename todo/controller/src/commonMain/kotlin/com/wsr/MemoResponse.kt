package com.wsr

data class MemoResponse(
    val id: MemoResponseId,
    val title: String,
    val description: String,
    val items: List<ItemResponse>,
) {
    companion object {
        internal fun from(memo: Memo) = MemoResponse(
            id = MemoResponseId(memo.id.value),
            title = memo.title,
            description = memo.description,
            items = memo.items.map { ItemResponse.from(it) },
        )
    }
}

data class ItemResponse(
    val id: ItemResponseId,
    val title: String,
    val description: String,
    val checked: Boolean,
) {
    companion object {
        internal fun from(item: Item) = ItemResponse(
            id = ItemResponseId(item.id.value),
            title = item.title,
            description = item.description,
            checked = item.checked,
        )
    }
}

@JvmInline
value class MemoResponseId(val value: String)

@JvmInline
value class ItemResponseId(val value: String)

internal fun MemoResponse.toJsonString(): String {
    val items = items.joinToString(
        separator = "\n",
        prefix = "{\n",
        postfix = "}\n",
    ) { item ->
        """
        {
            id: ${item.id.value}
            title: ${item.title},
            description: ${item.description},
            checked: ${item.checked}
        }
        """.trimIndent()
    }
    return """
    {
        id: ${id.value},
        title: ${title},
        description: ${description},
        items: $items
    }
    """.trimIndent()
}
