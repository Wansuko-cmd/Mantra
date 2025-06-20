package com.wsr.assistant

import dev.shreyaspatil.ai.client.generativeai.type.FunctionCallPart
import dev.shreyaspatil.ai.client.generativeai.type.FunctionDeclaration
import dev.shreyaspatil.ai.client.generativeai.type.Schema
import dev.shreyaspatil.ai.client.generativeai.type.Tool
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ListToolsResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val MCP_CLIENT_NAME = "mcp-client"
private const val MCP_CLIENT_VERSION = "1.0.0"

class McpClient private constructor(
    val client: Client,
    val tools: List<Tool>,
) {
    suspend fun callTool(part: FunctionCallPart): Content.Tool {
        val result = client.callTool(
                name = part.name,
                arguments = part.args.orEmpty(),
            )
        val content = result?.content?.joinToString("\n") { (it as TextContent).text.orEmpty() }
        return Content.Tool(
            name = part.name,
            result = content.orEmpty(),
        )
    }

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
                .map { (name, element) -> element.jsonObject.toSchema(name) },
            requiredParameters = tool.inputSchema.required.orEmpty(),
        )
    }
    return listOf(Tool(functionDeclarations = declarations))
}

private fun JsonObject.toSchema(name: String): Schema<out Any> =
    when (val type = jsonObject["type"]!!.jsonPrimitive.content) {
        "object" -> {
            val properties = jsonObject["properties"]!!.jsonObject
            Schema.obj(
                name = name,
                description = "",
                contents = properties
                    .map { (name, element) -> element.jsonObject.toSchema(name) }
                    .toTypedArray(),
            )
        }

        "array" -> {
            val items = jsonObject["items"]!!.jsonObject
            Schema.arr(
                name = name,
                description = "",
                items = items.toSchema("items"),
            )
        }

        else -> {
            val description = jsonObject["description"]?.jsonPrimitive?.content.orEmpty()
            when (type) {
                "string" -> Schema.str(name, description)
                "integer" -> Schema.int(name, description)
                "long" -> Schema.long(name, description)
                "double" -> Schema.double(name, description)
                "bool" -> Schema.bool(name, description)
                else -> throw IllegalStateException("$type is not valid type at Json.")
            }
        }
    }
