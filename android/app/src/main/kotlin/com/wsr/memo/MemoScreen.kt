package com.wsr.memo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wsr.memo.index.MemoIndexScreen
import com.wsr.memo.show.MemoShowScreen

@Composable
internal fun MemoScreen() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = MemoRoute.Index,
    ) {
        composable<MemoRoute.Index> {
            MemoIndexScreen(
                navigateToShow = { memoId ->
                    val route = MemoRoute.Show.create(memoId)
                    controller.navigate(route)
                },
            )
        }
        composable<MemoRoute.Show> { backStackEntry ->
            val memoId = backStackEntry.toRoute<MemoRoute.Show>().memoId
            MemoShowScreen(memoId = memoId, onBackPress = controller::popBackStack)
        }
    }
}
