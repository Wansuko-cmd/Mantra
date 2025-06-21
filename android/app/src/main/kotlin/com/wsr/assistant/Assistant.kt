package com.wsr.assistant

import com.wsr.MemoController
import com.wsr.mcp.setUpMcpServer
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.FunctionCallPart
import dev.shreyaspatil.ai.client.generativeai.type.TextPart
import dev.shreyaspatil.ai.client.generativeai.type.content

class Assistant private constructor(private val client: McpClient) {
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "",
        tools = client.tools,
        systemInstruction = content("user") {
            text(
                """
                あなたは優秀な秘書です
                主に話を聞いた上で内容をまとめ、メモ帳に整理していくことを業務としています
                
                ここで、メモに関するドメイン知識について記載します
                主に以下の二つの要素が存在します
                
                >メモ
                ** 概要 **
                TODOをまとめるために利用します
                まとめられているTODOの共通点を元にしてタイトルおよび説明文を決定します
                
                ** 要素 **
                id -> メモの識別子(TODOを作成する際にのみ利用する)
                title -> メモのタイトル
                description -> メモの説明文
                
                > TODO(アイテム)
                ** 概要 **
                TODOを表します
                また、アイテムとも呼ばれます
                メモに紐づいており、同じようなTODOがまとめられています
                                
                ** 要素 **
                title -> TODOのタイトル
                description -> TODOの説明文
                checked -> 既に完了させたかどうか
                memo_id -> 紐づいているメモの識別子
                
                ---------------------------------
                > 関係
                メモとTODOは一対多の関係にあります
                
                これらの内容を元に、話を聞きながらメモ帳を整理していきましょう
            """,
            )
        },
    )

    suspend fun send(message: String, history: List<Content> = emptyList()): List<Content> {
        val content = Content.User(part = Part.Text(message))
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

            else -> history + content + Content.AI(
                part = Part.Text(response.text.orEmpty()),
            )
        }
    }

    private suspend fun FunctionCallPart.call(): List<Content> {
        val response = client.callTool(this)
        return listOf(response)
    }

    companion object {
        private var instance: Assistant? = null
        suspend fun create(controller: MemoController): Assistant =
            instance ?: run {
                val transport = setUpMcpServer(controller)
                val client = McpClient.connect(transport)
                Assistant(client).also { instance = it }
            }
    }
}

private fun Content.toGeminiContent() = when (this) {
    is Content.User -> content(role = "user") { part(part.toGeminiPart()) }
    is Content.Tool -> content(role = "user") { part(part.toGeminiPart()) }
    is Content.AI -> content(role = "model") { part(part.toGeminiPart()) }
}

private fun Part.toGeminiPart() = when (this) {
    is Part.Text -> TextPart(value)
}
