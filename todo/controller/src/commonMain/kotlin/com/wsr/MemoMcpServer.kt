package com.wsr

import io.ktor.utils.io.streams.asInput
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import java.net.ServerSocket
import java.net.Socket

private const val LOCAL_HOST = "127.0.0.1"
private const val PORT = 12345

suspend fun setUpMcpServer(controller: MemoController): Transport {
    val (serverTransport, clientTransport) = createTransports()

    val server = createServer()
        .addMemoReadTool(controller)
        .addMemoCreateTool(controller)

    server.connect(serverTransport)

    return clientTransport
}

private suspend fun createTransports(): Pair<StdioServerTransport, StdioClientTransport> =
    withContext(Dispatchers.Default) {
        val server = async {
            ServerSocket(PORT).accept().let { socket ->
                StdioServerTransport(
                    inputStream = socket.inputStream.asInput(),
                    outputStream = socket.outputStream.asSink().buffered(),
                )
            }
        }

        val client = async {
            Socket(LOCAL_HOST, PORT).let { socket ->
                StdioClientTransport(
                    socket.inputStream.asInput(),
                    socket.outputStream.asSink().buffered(),
                )
            }
        }
        server.await() to client.await()
    }

private fun createServer() = Server(
    serverInfo = Implementation(
        name = "memo-server",
        version = "1.0.0",
    ),
    options = ServerOptions(
        capabilities = ServerCapabilities(
            resources = ServerCapabilities.Resources(
                subscribe = true,
                listChanged = true,
            ),
            tools = ServerCapabilities.Tools(listChanged = true),
        ),
    ),
)

private fun Server.addMemoReadTool(controller: MemoController): Server = this.apply {
    addTool(
        name = "read_memo",
        description = """
            Read Memos which bundle some todos.
        """.trimIndent(),
        inputSchema = Tool.Input(),
    ) { _ ->
        val memos = controller.getAll().first()
        CallToolResult(content = memos.map { memo -> TextContent(memo.toString()) })
    }
}

private fun Server.addMemoCreateTool(controller: MemoController): Server = this.apply {
    addTool(
        name = "create_memo",
        description = """
            Create Memo which bundle some todos.
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("title") {
                    put("type", "string")
                }
                putJsonObject("description") {
                    put("type", "string")
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
                content = listOf(TextContent("success.")),
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
