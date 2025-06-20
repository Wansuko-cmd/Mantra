package com.wsr.mcp

import com.wsr.MemoController
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

internal fun Server.applyMemoTools(controller: MemoController) = this.apply {
    getMemosTool(controller)
    createMemoTool(controller)
}

private fun Server.getMemosTool(controller: MemoController): Server = this.apply {
    addTool(
        name = "get_memos",
        description = """
            登録されている全てのメモを返します
        """.trimIndent(),
        inputSchema = Tool.Input(),
    ) { _ ->
        val memos = controller.getAll().first()
        CallToolResult(
            content = memos.map { memo ->
                val items = memo.items.joinToString("\n") { item ->
                    """
                    {
                        title: ${item.title},
                        description: ${item.description},
                        checked: ${item.description}
                    }
                    """.trimIndent()
                }
                TextContent(
                    """
                    {
                        id: ${memo.id},
                        title: ${memo.title},
                        description: ${memo.description},
                        items: {
                            $items
                        }
                    }
                    """.trimIndent(),
                )
            },
        )
    }
}

private fun Server.createMemoTool(controller: MemoController): Server = this.apply {
    addTool(
        name = "create_memo",
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
            required = listOf("title", "description"),
        ),
    ) { request ->
        val title = request.arguments["title"]?.jsonPrimitive?.contentOrNull
        val description = request.arguments["description"]?.jsonPrimitive?.contentOrNull
        if (title != null && description != null) {
            controller.create(
                title = title,
                description = description,
            )
            CallToolResult(
                content = listOf(TextContent("作成しました")),
            )
        } else {
            CallToolResult(
                content = listOf(
                    TextContent("The 'title' and 'description' parameters are required."),
                ),
            )
        }
    }
}
