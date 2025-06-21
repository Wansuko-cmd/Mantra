package com.wsr.mcp

import io.modelcontextprotocol.kotlin.sdk.GetPromptResult
import io.modelcontextprotocol.kotlin.sdk.Prompt
import io.modelcontextprotocol.kotlin.sdk.PromptMessage
import io.modelcontextprotocol.kotlin.sdk.Role
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.server.Server

internal fun Server.applyPrompts() = this.apply {
    addTodoPromptsDisorderly()
}

private fun Server.addTodoPromptsDisorderly() = this.apply {
    addPrompt(
        prompt = Prompt(
            name = "add_todo_prompts_disorderly",
            description = """
                TODOを乱雑に加える際に使うプロンプト
            """.trimIndent(),
            arguments = null,
        ),
    ) {
        val prompt = """
            これから色々なTODOを追加していきたいと思います
            ステップとしては以下の通りです
            1. ${CREATE_MEMO}を利用して「仮置き」というメモを作成
            2. ユーザーが書いた内容を元に、${CREATE_MEMO_ITEM}を利用して「仮置き」にTODOを追加
            3. 追加したTODOをいくつかのグループに分割し、${CREATE_MEMO}を利用して対応するメモを作成する
            4. ${MOVE_MEMO_ITEM}を利用して分割したメモに対応するTODOを移動させる
        """.trimIndent()
        GetPromptResult(
            description = "",
            messages = listOf(
                PromptMessage(
                    role = Role.user,
                    content = TextContent(prompt),
                ),
            ),
        )
    }
}
