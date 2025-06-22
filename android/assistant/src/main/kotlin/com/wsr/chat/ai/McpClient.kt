package com.wsr.chat.ai

import io.modelcontextprotocol.kotlin.sdk.GetPromptRequest
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.Role
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.shared.Transport

private const val MCP_CLIENT_NAME = "mcp-client"
private const val MCP_CLIENT_VERSION = "1.0.0"

internal class McpClient private constructor(
    private val client: Client,
    val tools: List<ToolInfo>,
    val prompts: List<PromptInfo>,
) {
    suspend fun getPrompt(name: String, args: Map<String, String>? = null): List<Content> {
        val messages = client
            .getPrompt(request = GetPromptRequest(name = name, arguments = args))
            ?.messages
            .orEmpty()
        return messages.map { message ->
            val part = when (val content = message.content) {
                is TextContent -> Part.Text(content.text.orEmpty())
                else -> TODO("Implement part branch.")
            }
            when (message.role) {
                Role.user -> Content.User(part)
                Role.assistant -> Content.AI(part)
            }
        }
    }
    suspend fun callTool(name: String, args: Map<String, Any?>): Content.Tool {
        val result = client.callTool(
            name = name,
            arguments = args,
        )
        val content = result?.content?.joinToString("\n") { (it as TextContent).text.orEmpty() }
        return Content.Tool(
            name = name,
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
            val tools = client.listTools()?.tools.orEmpty()
            val prompts = client.listPrompts()?.prompts.orEmpty()
            return McpClient(client = client, tools = tools, prompts = prompts)
        }
    }
}
