package com.wsr.assistant

sealed interface Content {
    val part: Part
    data class User(override val part: Part) : Content
    data class Tool(override val part: Part) : Content
    data class AI(override val part: Part) : Content
}

sealed interface Part {
    data class Text(val value: String) : Part
}