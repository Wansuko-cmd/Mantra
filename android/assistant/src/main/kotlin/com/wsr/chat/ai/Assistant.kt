package com.wsr.chat.ai

import kotlinx.coroutines.flow.Flow

internal interface Assistant {
    val promptInfos: List<PromptInfo>
    fun sendPrompt(
        name: String,
        args: Map<String, String>? = null,
        history: List<Content> = emptyList(),
    ): Flow<List<Content>>
    fun send(message: String, history: List<Content> = emptyList()): Flow<List<Content>>
}

internal typealias PromptInfo = io.modelcontextprotocol.kotlin.sdk.Prompt
internal typealias ToolInfo = io.modelcontextprotocol.kotlin.sdk.Tool

internal sealed interface Content {
    val part: Part

    data class User(override val part: Part) : Content
    data class Tool(val name: String, val result: String) : Content {
        override val part: Part = Part.Text(
            """
                "type": "tool_result",
                "tool_name": $name,
                "result": $result
            """.trimIndent(),
        )
    }

    data class AI(override val part: Part) : Content
}

internal sealed interface Part {
    data class Text(val value: String) : Part
}
