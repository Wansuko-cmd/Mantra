package com.wsr.assistant

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.FunctionCallPart
import dev.shreyaspatil.ai.client.generativeai.type.TextPart
import dev.shreyaspatil.ai.client.generativeai.type.content

class AI(private val client: McpClient) {
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "",
        tools = client.tools,
    )

    suspend fun send(message: String, history: List<Content> = emptyList()): List<Content> {
        val content = Content(role = Role.User, part = Part.Text(message))
        return send(content, history)
    }

    private tailrec suspend fun send(content: Content, history: List<Content>): List<Content> {
        val response = model
            .startChat(history.map { it.toGeminiContent() })
            .sendMessage(content.toGeminiContent())

        return when {
            response.functionCalls.isNotEmpty() -> {
                val result = response.functionCalls.flatMap { it.call() }
                send(
                    content = result.last(),
                    history = history + content + result.dropLast(1),
                )
            }

            else -> history + content + Content(
                role = Role.AI,
                part = Part.Text(response.text.orEmpty()),
            )
        }
    }

    private suspend fun FunctionCallPart.call(): List<Content> {
        val request = Content(
            role = Role.AI,
            part = Part.Text("[Calling tool $name with args $args]"),
        )
        val result = client.callTool(this)
        val response = Content(role = Role.Tool, part = Part.Text(result))
        return listOf(request, response)
    }
}

private fun Content.toGeminiContent() = when (role) {
    is Role.User -> content(role = "user") { part(part.toGeminiPart()) }
    is Role.Tool -> content(role = "user") { part(part.toGeminiPart()) }
    is Role.AI -> content(role = "model") { part(part.toGeminiPart()) }
}

private fun Part.toGeminiPart() = when (this) {
    is Part.Text -> TextPart(value)
}
