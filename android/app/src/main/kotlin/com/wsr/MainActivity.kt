package com.wsr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wsr.assistant.AssistantRoute
import com.wsr.memo.index.MemoIndexRoute
import com.wsr.memo.show.MemoShowRoute
import com.wsr.theme.MantraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MantraTheme {
                val controller = rememberNavController()
                NavHost(
                    navController = controller,
                    startDestination = Route.Assistant,
                ) {
                    composable<Route.Memo.Index> {
                        MemoIndexRoute(
                            navigateToShow = { memoId ->
                                val route = Route.Memo.Show.create(memoId)
                                controller.navigate(route)
                            },
                        )
                    }
                    composable<Route.Memo.Show> { backStackEntry ->
                        val memoId = backStackEntry.toRoute<Route.Memo.Show>().memoId
                        MemoShowRoute(memoId)
                    }
                    composable<Route.Assistant> {
                        AssistantRoute()
                    }
                }
            }
        }
    }
}
