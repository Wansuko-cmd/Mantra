package com.wsr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.wsr.theme.MantraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MantraTheme {
                LPScreen()
            }
        }
    }

    @Composable
    private fun LPScreen() {
        val pager = LPRoute.entries
        val state = rememberPagerState(initialPage = 0) { pager.size }
        HorizontalPager(state) { index ->
            when (pager[index]) {
                LPRoute.Assistant -> AssistantScreen()
                LPRoute.Memo -> MemoScreen()
            }
        }
    }

    private enum class LPRoute {
        Assistant,
        Memo,
    }
}
