package com.wsr.mcp

import com.wsr.ItemResponseId
import com.wsr.MemoController
import com.wsr.MemoResponseId
import com.wsr.toJsonString
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

const val CREATE_MEMO_ITEM = "create_memo_item"
const val UPDATE_MEMO_ITEM = "update_memo_item"
const val MOVE_MEMO_ITEM = "move_memo_item"

internal fun Server.applyMemoItemTools(controller: MemoController) = this.apply {
    createMemoItemTool(controller)
    updateMemoItemTool(controller)
    moveMemoItemTool(controller)
}

private fun Server.createMemoItemTool(controller: MemoController) = this.apply {
    addTool(
        name = CREATE_MEMO_ITEM,
        description = """
            指定されたメモに紐づく新たなTODOを作成します
            指定するために用いるIDは${GET_MEMOS}で確認可能です
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
            required = listOf("memo_id", "title"),
        ),
    ) { request ->
        val memoId = request.arguments["memo_id"]?.jsonPrimitive?.contentOrNull
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull.orEmpty()
        if (memoId == null || title == null) {
            val content = TextContent("memo_id, titleは必須です")
            return@addTool CallToolResult(content = listOf(content))
        }
        try {
            val memo = controller.addItem(
                id = MemoResponseId(memoId),
                title = title,
                description = description,
            )
            val context = TextContent("追加後のメモ: ${memo.toJsonString()}")
            CallToolResult(content = listOf(context))
        } catch (e: Exception) {
            CallToolResult(content = listOf(TextContent("エラー: $e")))
        }
    }
}

private fun Server.updateMemoItemTool(controller: MemoController) = this.apply {
    addTool(
        name = UPDATE_MEMO_ITEM,
        description = """
            指定されたメモ上の、指定されたTODOを更新します
            指定するために用いるIDは${GET_MEMOS}で確認可能です
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("memo_id") {
                    put("type", "string")
                    put("description", "紐づいているメモの識別子")
                }
                putJsonObject("item_id") {
                    put("type", "string")
                    put("description", "更新するTODOのID")
                }
                putJsonObject("title") {
                    put("type", "string")
                    put("description", "更新するTODOのタイトル")
                }
                putJsonObject("description") {
                    put("type", "string")
                    put("description", "更新するTODOの説明文")
                }
                putJsonObject("checked") {
                    put("type", "bool")
                    put("description", "更新するTODOのチェック状態")
                }
            },
            required = listOf("memo_id", "item_id"),
        ),
    ) { request ->
        val memoId = request.arguments["memo_id"]?.jsonPrimitive?.contentOrNull
        val itemId = request.arguments["item_id"]?.jsonPrimitive?.contentOrNull
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull
        val checked = request.arguments["checked"]?.jsonPrimitive?.booleanOrNull
        if (memoId == null || itemId == null) {
            val content = TextContent("memo_id, item_idは必須です")
            return@addTool CallToolResult(content = listOf(content))
        }
        try {
            val memo = controller.updateItem(
                memoId = MemoResponseId(memoId),
                itemId = ItemResponseId(itemId),
                title = title,
                description = description,
                checked = checked,
            )
            val context = TextContent("更新後のメモ: ${memo.toJsonString()}")
            CallToolResult(content = listOf(context))
        } catch (e: Exception) {
            CallToolResult(content = listOf(TextContent("エラー: $e")))
        }
    }
}

private fun Server.moveMemoItemTool(controller: MemoController) = this.apply {
    addTool(
        name = MOVE_MEMO_ITEM,
        description = """
            指定されたTODOと紐づくメモをfrom -> toに変更します
            指定するために用いるIDは${GET_MEMOS}で確認可能です
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("item_id") {
                    put("type", "string")
                    put("description", "更新するTODOのID")
                }
                putJsonObject("from_memo_id") {
                    put("type", "string")
                    put("description", "紐づけ元のメモの識別子")
                }
                putJsonObject("to_memo_id") {
                    put("type", "string")
                    put("description", "紐づけ先のメモの識別子")
                }
            },
            required = listOf("item_id", "from_memo_id", "to_memo_id"),
        ),
    ) { request ->
        val itemId = request.arguments["item_id"]?.jsonPrimitive?.contentOrNull
        val fromId = request.arguments["from_memo_id"]?.jsonPrimitive?.contentOrNull
        val toId = request.arguments["to_memo_id"]?.jsonPrimitive?.contentOrNull

        if (itemId == null || fromId == null || toId == null) {
            val content = TextContent("memo_id, item_idは必須です")
            return@addTool CallToolResult(content = listOf(content))
        }
        try {
            val memo = controller.moveItem(
                itemId = ItemResponseId(itemId),
                from = MemoResponseId(fromId),
                to = MemoResponseId(toId),
            )
            val context = TextContent("移動先のメモ: ${memo.toJsonString()}")
            CallToolResult(content = listOf(context))
        } catch (e: Exception) {
            CallToolResult(content = listOf(TextContent("エラー: $e")))
        }
    }
}

