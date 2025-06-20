package com.wsr.mcp

import com.wsr.MemoController
import com.wsr.MemoResponseId
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

internal fun Server.applyMemoItemTools(controller: MemoController) = this.apply {
    createMemoItemsTool(controller)
}

private fun Server.createMemoItemsTool(controller: MemoController) = this.apply {
    addTool(
        name = "create_memo_item",
        description = """
            新たなTODOを作成します
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("memo_id") {
                    put("type", "string")
                    put("description", "紐づけるメモの識別子")
                }
                putJsonObject("title") {
                    put("type", "string")
                    put("description", "作成するTODOのタイトル")
                }
                putJsonObject("description") {
                    put("type", "string")
                    put("description", "作成するTODOの説明文")
                }
            },
            required = listOf("memo_id"),
        ),
    ) { request ->
        val memoId = request.arguments["memo_id"]?.jsonPrimitive?.contentOrNull
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull
        if (memoId == null || title == null || description == null) {
            return@addTool CallToolResult(
                content = listOf(
                    TextContent("memo_id, title, descriptionは必須です"),
                ),
            )
        }
        try {
            controller.addItem(
                id = MemoResponseId(memoId),
                title = title,
                description = description,
            )
            CallToolResult(content = listOf(TextContent("成功")))
        } catch (e: Exception) {
            CallToolResult(content = listOf(TextContent("失敗")))
        }
    }
}
