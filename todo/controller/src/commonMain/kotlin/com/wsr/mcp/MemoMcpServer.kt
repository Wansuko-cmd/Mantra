package com.wsr.mcp

import com.wsr.MemoController
import io.ktor.utils.io.streams.asInput
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.shared.Transport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.io.asSink
import kotlinx.io.buffered
import java.net.ServerSocket
import java.net.Socket

private const val LOCAL_HOST = "127.0.0.1"
private const val PORT = 12345
private val serverSocket = ServerSocket(PORT)

suspend fun setUpMcpServer(controller: MemoController): Transport {
    val (serverTransport, clientTransport) = createTransports()

    val server = createServer()
        .applyMemoTools(controller)

    server.connect(serverTransport)

    return clientTransport
}

private suspend fun createTransports(): Pair<StdioServerTransport, StdioClientTransport> =
    withContext(Dispatchers.Default) {
        val server = async {
            serverSocket.accept().let { socket ->
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
            prompts = ServerCapabilities.Prompts(listChanged = true),
            tools = ServerCapabilities.Tools(listChanged = true),
        ),
    ),
)
