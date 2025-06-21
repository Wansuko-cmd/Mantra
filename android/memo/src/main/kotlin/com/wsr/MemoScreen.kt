package com.wsr

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wsr.index.IndexScreen
import com.wsr.show.ShowScreen

@Composable
fun MemoScreen() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = MemoRoute.Index,
    ) {
        composable<MemoRoute.Index> {
            IndexScreen(
                navigateToShow = { memoId ->
                    val route = MemoRoute.Show.create(memoId)
                    controller.navigate(route)
                },
            )
        }
        composable<MemoRoute.Show> { backStackEntry ->
            val memoId = backStackEntry.toRoute<MemoRoute.Show>().memoId
            ShowScreen(memoId = memoId, onBackPress = controller::popBackStack)
        }
    }
}
