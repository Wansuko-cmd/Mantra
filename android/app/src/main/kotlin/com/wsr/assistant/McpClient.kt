package com.wsr.assistant

import com.google.ai.client.generativeai.type.FunctionCallPart
import com.google.ai.client.generativeai.type.FunctionDeclaration
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.Tool
import io.modelcontextprotocol.kotlin.sdk.CallToolResultBase
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ListToolsResult
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.shared.Transport

private const val MCP_CLIENT_NAME = "mcp-client"
private const val MCP_CLIENT_VERSION = "1.0.0"

class McpClient private constructor(
    val client: Client,
    val tools: List<Tool>,
) {
    suspend fun callTool(part: FunctionCallPart): CallToolResultBase? = client.callTool(
        name = part.name,
        arguments = part.args,
    )

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
            parameters = tool.inputSchema.properties.map { property ->
                Schema(
                    name = property.key,
                    description = "",
                    type = when (tool.inputSchema.type) {
                        "object" -> FunctionType.OBJECT
                        "array" -> FunctionType.ARRAY
                        else -> FunctionType.STRING
                    },
                )
            },
            requiredParameters = tool.inputSchema.required.orEmpty(),
        )
    }
    return listOf(Tool(functionDeclarations = declarations))
}
