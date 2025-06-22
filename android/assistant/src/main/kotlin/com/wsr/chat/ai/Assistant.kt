package com.wsr.chat.ai

internal interface Assistant {
    suspend fun send(message: String, history: List<Content> = emptyList()): List<Content>
}

internal sealed interface Content {
    val part: Part

    data class User(override val part: Part) : Content
    data class Tool(
        val name: String,
        val result: String,
    ) : Content {
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
