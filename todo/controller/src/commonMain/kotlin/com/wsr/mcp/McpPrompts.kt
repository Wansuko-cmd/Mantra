package com.wsr.mcp

import io.modelcontextprotocol.kotlin.sdk.GetPromptResult
import io.modelcontextprotocol.kotlin.sdk.Prompt
import io.modelcontextprotocol.kotlin.sdk.PromptMessage
import io.modelcontextprotocol.kotlin.sdk.Role
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.server.Server

internal fun Server.applyPrompts() = this.apply {
    addTodoDisorderlyPrompt()
    readTodoPrompt()
    sortOutTodosPrompt()
}

private fun Server.addTodoDisorderlyPrompt() = this.apply {
    addPrompt(
        prompt = Prompt(
            name = "TODO乱雑追加用プロンプト",
            description = """
                とにかくTODOを書き込みたい！というときに利用
                以下のステップに従って処理を進めます

                1. AIが「仮置き」というメモを作成
                2. あなたがTODOを記述していき、AIが「仮置き」に追加していく
                3. AIが追加したTODOをいくつかのグループに分割し、対応するメモを作成する
                4. 2で作成したTODOを対応するメモに移動させる
                5. AIが仮置きメモを削除
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
            5. ${DELETE_MEMO}を利用して、1で作成した「仮置き」メモを削除する
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

private fun Server.readTodoPrompt() = this.apply {
    addPrompt(
        prompt = Prompt(
            name = "TODO要約用",
            description = """
                TODOの要約をパッと読みたいときに利用
            """.trimIndent(),
            arguments = null,
        ),
    ) {
        val prompt = """
            これから以下のステップに従ってTODOの要約をしてください
            1. ${GET_MEMOS}を利用して存在するメモ、およびTODOを把握する
            2. 存在するTODOのうち、チェックがついていないものを数える
            3. メモを、チェックがついていないTODOが多い順に並べる
            4. メモのタイトル、説明文、チェックのついていないTODOのタイトルを順番に説明する
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

private fun Server.sortOutTodosPrompt() = this.apply {
    addPrompt(
        prompt = Prompt(
            name = "TODO整理用",
            description = """
                TODOとメモの関係を整理する際に利用
            """.trimIndent(),
            arguments = null,
        ),
    ) {
        val prompt = """
            これからメモとTODOの紐付けを見直したいと考えています
            以下のステップに従ってTODOの整理をしてください
            1. ${GET_MEMOS}を利用してメモとTODOを取得する
            2. メモのタイトルと説明文を読み、どのようなTODOが紐づくメモなのかを理解する
            3. 2で確認したメモと紐づくTODOのタイトルと説明文を読み、意味合いが異なるものを抽出する
            4. 3で抽出したTODOについて、どのメモに紐付けるのがいいか検討する
            5-a. 適切なメモが存在した場合、そのメモに追加する旨を提案する
            5-b. 適切なメモが存在しなかった場合、以下のステップを踏む
            5-b-1. そのTODOに相応しいメモの名前を提案する
            5-b-2. 了承を得たら、提案した名前を元にメモを作成する
            5-b-3. 作成したメモにTODOを追加する
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
