package com.wsr

import androidx.datastore.core.okio.OkioSerializer
import okio.BufferedSink
import okio.BufferedSource

inline fun <reified T> createSerializer(): OkioSerializer<List<T>> = createSerializer(emptyList())

inline fun <reified T> createSerializer(defaultValue: T) = object : OkioSerializer<T> {
    override val defaultValue: T
        get() = defaultValue

    override suspend fun readFrom(source: BufferedSource): T {
        return json.decodeFromString<T>(source.readUtf8())
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(json.encodeToString(t))
        }
    }
}
