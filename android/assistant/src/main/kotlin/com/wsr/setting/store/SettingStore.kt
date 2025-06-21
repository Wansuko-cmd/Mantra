package com.wsr.setting.store

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.wsr.createSerializer
import kotlinx.serialization.Serializable
import okio.FileSystem
import okio.Path.Companion.toPath

class SettingStore(private val producePath: (name: String) -> String) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            serializer = createSerializer(defaultValue = SettingModel()),
            fileSystem = FileSystem.SYSTEM,
            producePath = { producePath("setting").toPath() },
        ),
    )

    val data get() = db.data

    suspend fun update(transform: suspend (SettingModel) -> SettingModel) = db.updateData(transform)
}

@Serializable
data class SettingModel(
    val apiKey: String = "",
    val prompt: String = "",
)
