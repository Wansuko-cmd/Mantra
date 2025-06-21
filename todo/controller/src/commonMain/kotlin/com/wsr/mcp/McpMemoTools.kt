package com.wsr.mcp

import com.wsr.MemoController
import com.wsr.toJsonString
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

const val GET_MEMOS = "get_memos"
const val CREATE_MEMO = "create_memo"

internal fun Server.applyMemoTools(controller: MemoController) = this.apply {
    getMemosTool(controller)
    createMemoTool(controller)
}

private fun Server.getMemosTool(controller: MemoController): Server = this.apply {
    addTool(
        name = GET_MEMOS,
        description = """
            登録されている全てのメモを返します
        """.trimIndent(),
        inputSchema = Tool.Input(),
    ) { _ ->
        val memos = controller.getAll().first()
        CallToolResult(
            content = memos
                .map { memo -> memo.toJsonString() }
                .map { TextContent(it) },
        )
    }
}

private fun Server.createMemoTool(controller: MemoController): Server = this.apply {
    addTool(
        name = CREATE_MEMO,
        description = """
            新たなメモを作成します
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("title") {
                    put("type", "string")
                    put("description", "The title of memo you will create.")
                }
                putJsonObject("description") {
                    put("type", "string")
                    put("description", "The description of memo you will create.")
                }
            },
            required = listOf("title"),
        ),
    ) { request ->
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull.orEmpty()
        if (title != null) {
            val memo = controller.create(
                title = title,
                description = description,
            )
            val content = TextContent("追加したメモ: ${memo.toJsonString()}")
            CallToolResult(content = listOf(content))
        } else {
            val content = TextContent("titleは必須です")
            CallToolResult(content = listOf(content))
        }
    }
}
