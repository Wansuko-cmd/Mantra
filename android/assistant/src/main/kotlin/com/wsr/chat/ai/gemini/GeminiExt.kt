package com.wsr.chat.ai.gemini

import dev.shreyaspatil.ai.client.generativeai.type.FunctionDeclaration
import dev.shreyaspatil.ai.client.generativeai.type.Schema
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private typealias McpTool = io.modelcontextprotocol.kotlin.sdk.Tool
private typealias GeminiTool = dev.shreyaspatil.ai.client.generativeai.type.Tool

internal fun List<McpTool>.toGeminiTools(): List<GeminiTool> {
    val declarations = this.map { tool -> tool.toFunctionDeclaration() }
    return listOf(GeminiTool(declarations))
}

private fun McpTool.toFunctionDeclaration() = FunctionDeclaration(
    name = name,
    description = description.orEmpty(),
    parameters = inputSchema.properties
        .map { (name, element) -> element.jsonObject.toSchema(name) },
    requiredParameters = inputSchema.required.orEmpty(),
)

private fun JsonObject.toSchema(name: String): Schema<out Any> =
    when (val type = jsonObject["type"]!!.jsonPrimitive.content) {
        "object" -> {
            val properties = jsonObject["properties"]!!.jsonObject
            Schema.obj(
                name = name,
                description = "",
                contents = properties
                    .map { (name, element) -> element.jsonObject.toSchema(name) }
                    .toTypedArray(),
            )
        }

        "array" -> {
            val items = jsonObject["items"]!!.jsonObject
            Schema.arr(
                name = name,
                description = "",
                items = items.toSchema("items"),
            )
        }

        else -> {
            val description = jsonObject["description"]?.jsonPrimitive?.content.orEmpty()
            when (type) {
                "string" -> Schema.str(name, description)
                "integer" -> Schema.int(name, description)
                "long" -> Schema.long(name, description)
                "double" -> Schema.double(name, description)
                "bool" -> Schema.bool(name, description)
                else -> throw IllegalStateException("$type is not valid type at Json.")
            }
        }
    }
