package com.wsr.assistant

sealed interface Content {
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

sealed interface Part {
    data class Text(val value: String) : Part
}