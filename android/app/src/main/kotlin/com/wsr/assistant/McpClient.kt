package com.wsr.assistant

import android.util.Log
import dev.shreyaspatil.ai.client.generativeai.type.FunctionCallPart
import dev.shreyaspatil.ai.client.generativeai.type.FunctionDeclaration
import dev.shreyaspatil.ai.client.generativeai.type.Schema
import dev.shreyaspatil.ai.client.generativeai.type.Tool
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ListToolsResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

private const val MCP_CLIENT_NAME = "mcp-client"
private const val MCP_CLIENT_VERSION = "1.0.0"

class McpClient private constructor(
    val client: Client,
    val tools: List<Tool>,
) {
    suspend fun callTool(part: FunctionCallPart): String =
        client
            .callTool(
                name = part.name,
                arguments = part.args.orEmpty(),
            )
            ?.let { result ->
                val content = result.content
                    .joinToString("\n") { (it as TextContent).text ?: "" }
                """
                    "type": "tool_result",
                    "tool_name": ${part.name},
                    "result": $content
                """.trimIndent()
            }
            .orEmpty()

    companion object {
        suspend fun connect(transport: Transport): McpClient {
            val client = Client(
                clientInfo = Implementation(
                    name = MCP_CLIENT_NAME,
                    version = MCP_CLIENT_VERSION,
                ),
            )
            client.connect(transport)
            val tools = client.listTools()?.toTools().orEmpty()
            return McpClient(client = client, tools = tools)
        }
    }
}


private fun ListToolsResult.toTools(): List<Tool> {
    val declarations = tools.map { tool ->
        FunctionDeclaration(
            name = tool.name,
            description = tool.description.orEmpty(),
            parameters = tool.inputSchema.properties
                .map { (name, element) -> element.toSchema(name) },
            requiredParameters = tool.inputSchema.required.orEmpty(),
        )
    }
    return listOf(Tool(functionDeclarations = declarations))
}

// Please explain what kind of memos are there?
private fun JsonElement.toSchema(name: String): Schema<out Any> = when (this) {
    is JsonObject -> Schema.obj(
        name = name,
        description = "",
        contents = entries.map { (name, element) -> element.toSchema(name) }.toTypedArray(),
    ).also { Log.d("Here", "obj") }

    is JsonArray -> Schema.arr(
        name = name,
        description = "",
        items = this.first().toSchema(""),
    ).also { Log.d("Here", "arr") }

    is JsonPrimitive -> when {
        isString -> Schema.str(name = name, description = content)
        booleanOrNull != null -> Schema.bool(name = name, description = content)
        intOrNull != null -> Schema.int(name = name, description = content)
        longOrNull != null -> Schema.long(name = name, description = content)
        doubleOrNull != null -> Schema.double(name = name, description = content)
        else -> Schema.str(name = name, description = content)
    }.also { Log.d("Here", "pri") }
}
