package com.wsr

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.Serializable
import okio.FileSystem
import okio.Path.Companion.toPath

class MemoStore(private val producePath: () -> String) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage<List<MemoModel>>(
            serializer = createSerializer(),
            fileSystem = FileSystem.SYSTEM,
            producePath = { producePath().toPath() },
        ),
    )

    val data get() = db.data

    suspend fun create(memo: MemoModel) {
        db.updateData { it + memo }
    }

    suspend fun update(memo: MemoModel) {
        db.updateData { prevData ->
            val index = prevData.indexOfFirst { it.id == memo.id }
            prevData.subList(0, index) + memo + prevData.subList(index + 1, prevData.size)
        }
    }

    suspend fun delete(id: String) {
        db.updateData { prevData -> prevData.filter { it.id != id } }
    }
}

@Serializable
data class MemoModel(
    val id: String,
    val title: String,
    val description: String,
    val items: List<ItemModel>,
)

@Serializable
data class ItemModel(
    val id: String,
    val title: String,
    val description: String,
    val checked: Boolean,
)
