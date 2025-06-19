package com.wsr.assistant

data class Content(
    val role: Role,
    val part: Part,
)

sealed interface Role {
    data object User : Role
    data object Tool : Role
    data object AI : Role
}

sealed interface Part {
    data class Text(val value: String) : Part
}