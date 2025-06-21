package com.wsr.mcp

import com.wsr.MemoController
import com.wsr.MemoResponseId
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
const val UPDATE_MEMO = "update_memo"
const val DELETE_MEMO = "delete_memo"

internal fun Server.applyMemoTools(controller: MemoController) = this.apply {
    getMemosTool(controller)
    createMemoTool(controller)
    updateMemoTool(controller)
    deleteMemoTool(controller)
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

private fun Server.updateMemoTool(controller: MemoController): Server = this.apply {
    addTool(
        name = UPDATE_MEMO,
        description = """
            指定されたメモに付随する情報を更新します
            メモに紐づくアイテムを更新したい場合は${UPDATE_MEMO_ITEM}を利用してください
            指定するために用いるIDは${GET_MEMOS}で確認可能です
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("memo_id") {
                    put("type", "string")
                    put("description", "更新するメモの識別子")
                }
                putJsonObject("title") {
                    put("type", "string")
                    put("description", "更新後のメモのタイトル")
                }
                putJsonObject("description") {
                    put("type", "string")
                    put("description", "更新後のメモの説明文")
                }
            },
            required = listOf("memo_id"),
        ),
    ) { request ->
        val memoId = request.arguments["memo_id"]?.jsonPrimitive?.contentOrNull
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull
        if (memoId != null) {
            val memo = controller.update(
                id = MemoResponseId(memoId),
                title = title,
                description = description,
            )
            val content = TextContent("更新後のメモ: ${memo.toJsonString()}")
            CallToolResult(content = listOf(content))
        } else {
            val content = TextContent("titleは必須です")
            CallToolResult(content = listOf(content))
        }
    }
}

private fun Server.deleteMemoTool(controller: MemoController): Server = this.apply {
    addTool(
        name = DELETE_MEMO,
        description = """
            指定されたメモに付随する情報を削除します
            メモに紐づくアイテムを削除したい場合は${REMOVE_MEMO_ITEM}を利用してください
            指定するために用いるIDは${GET_MEMOS}で確認可能です
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("memo_id") {
                    put("type", "string")
                    put("description", "更新するメモの識別子")
                }
            },
            required = listOf("memo_id"),
        ),
    ) { request ->
        val memoId = request.arguments["memo_id"]?.jsonPrimitive?.contentOrNull
        if (memoId != null) {
            val id = controller.delete(id = MemoResponseId(memoId))
            val content = TextContent("削除したメモのID: ${id.value}")
            CallToolResult(content = listOf(content))
        } else {
            val content = TextContent("titleは必須です")
            CallToolResult(content = listOf(content))
        }
    }
}
