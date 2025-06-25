package com.wsr.chat.ai.gemini

import com.wsr.MemoController
import com.wsr.chat.ai.Assistant
import com.wsr.chat.ai.Content
import com.wsr.chat.ai.McpClient
import com.wsr.chat.ai.Part
import com.wsr.chat.ai.PromptInfo
import com.wsr.mcp.GET_MEMOS
import com.wsr.mcp.setUpMcpServer
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.FunctionCallPart
import dev.shreyaspatil.ai.client.generativeai.type.TextPart
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

internal class GeminiAssistant private constructor(
    private val client: McpClient,
    private val apiKey: String,
    private val prompt: String,
) : Assistant {
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = apiKey,
        tools = client.tools.toGeminiTools(),
        systemInstruction = content("user") {
            text(
                """
                まず、ユーザーからのプロンプトを以下に示します
                ```
                $prompt
                ```

                次にあなたが扱うメモ帳に関する知識を記述します
                ---------------------------------
                > ドメイン要素
                メモに関するドメイン知識について記載します
                主に以下の二つの要素が存在します
                
                *メモ*
                ** 概要 **
                TODOをまとめるために利用します
                まとめられているTODOの共通点を元にしてタイトルおよび説明文を決定します
                
                ** 要素 **
                id -> メモの識別子(TODOの作成、更新の際にのみ利用)
                title -> メモのタイトル
                description -> メモの説明文
                
                *TODO(アイテム)*
                ** 概要 **
                TODOを表します
                また、アイテムとも呼ばれます
                メモに紐づいており、同じようなTODOがまとめられています
                                
                ** 要素 **
                id -> TODOの識別子(TODOを更新するときにのみ利用)
                title -> TODOのタイトル
                description -> TODOの説明文
                checked -> 既に完了させたかどうか
                
                ---------------------------------
                > ドメインの関係性
                メモとTODOは一対多の関係にあります
                従ってTODOの作成・更新の際には基本的にメモのIDも必要になってきます

                ---------------------------------
                > 注意事項
                *確認を取るとき*
                ユーザーに作業の確認を取る際にはタイトル、もしくは説明文を使いましょう
                IDは使わないようにしてください
                
                *IDが分からない時
                メモ・TODOの作成・更新にはIDが必要になります
                IDは${GET_MEMOS}を利用することで取得可能です
                必要に応じて${GET_MEMOS}を実行し、対応したIDを取得した上で作成・更新を行いましょう
                ただし、ユーザーにIDを確認する行為は止めてください
                必ずタイトル、もしくは説明文で確認を取るようにしてください
                
                *文章のみ入力された場合*
                文章のみを入力されたときはTODOとして扱いましょう
                既にTODOとして存在する場合はTODOの内容について教えてください
                存在しない場合は以下のステップを踏んでTODOを作成してください
                1. ${GET_MEMOS}を用いてメモを取得する
                2. TODOを追加するのに相応しいメモを探す
                3-a. メモが存在した場合、そのメモに追加する旨を提案する
                3-b. メモが存在しなかった場合、以下のステップを踏む
                3-b-1. そのTODOに相応しいメモの名前を提案する
                3-b-2. 了承を得たら、提案した名前を元にメモを作成する
                3-b-3. 作成したメモにTODOを追加する

                ---------------------------------
                これらの内容を元に業務を行ってください
                まず最初に${GET_MEMOS}を用いて現在のメモの状態を確認しましょう（ユーザに確認は不要です）
                その後、要望に従ってメモ帳を整理していきましょう
            """,
            )
        },
    )

    override val promptInfos: List<PromptInfo> = client.prompts
    override fun sendPrompt(
        name: String,
        args: Map<String, String>?,
        history: List<Content>,
    ): Flow<List<Content>> = flow {
        val prompts = client.getPrompt(name, args)
        if (prompts.isNotEmpty()) {
            this.sendBuilder(content = prompts.last(), history = history + prompts.dropLast(1))
        } else {
            emit(history)
        }
    }

    override fun send(message: String, history: List<Content>): Flow<List<Content>> {
        val content = Content.User(part = Part.Text(message))
        return flow { sendBuilder(content, history) }
    }

    private suspend fun FlowCollector<List<Content>>.sendBuilder(
        content: Content,
        history: List<Content>,
    ) {
        emit(history + content)

        val response = model
            .startChat(history.map { it.toGeminiContent() })
            .sendMessage(content.toGeminiContent())

        when {
            response.functionCalls.isNotEmpty() -> {
                val result = response.functionCalls.flatMap { it.call() }
                emit(history + content + result.dropLast(1))
                sendBuilder(
                    content = result.last(),
                    history = history + content + result.dropLast(1),
                )
            }

            else -> {
                val respondContent = Content.AI(part = Part.Text(response.text.orEmpty()))
                emit(history + content + respondContent)
            }
        }
    }

    private suspend fun FunctionCallPart.call(): List<Content> {
        val response = client.callTool(name = name, args = args.orEmpty())
        return listOf(response)
    }

    companion object {
        @Volatile
        private var client: McpClient? = null
        suspend fun create(
            controller: MemoController,
            apiKey: String,
            prompt: String,
        ): GeminiAssistant {
            if (client == null) {
                val transport = setUpMcpServer(controller)
                client = McpClient.connect(transport)
            }
            return GeminiAssistant(
                client = client!!,
                apiKey = apiKey,
                prompt = prompt,
            )
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
