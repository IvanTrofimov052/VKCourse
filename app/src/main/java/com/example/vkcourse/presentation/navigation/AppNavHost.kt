package com.example.vkcourse.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkcourse.presentation.appdetails.AppDetailsRoute
import com.example.vkcourse.presentation.applist.AppListRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.APP_LIST,
        modifier = modifier,
    ) {
        composable(AppDestinations.APP_LIST) {
            AppListRoute(
                onAppClick = { app ->
                    navController.navigate("app/${Uri.encode(app.id)}") {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(
            route = AppDestinations.APP_DETAILS,
            arguments = listOf(
                navArgument(AppDestinations.APP_ID) { type = NavType.StringType },
            ),
        ) {
            // appId попадает в SavedStateHandle ViewModel через аргументы навигации.
            AppDetailsRoute(
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = {
                    navController.navigate(AppDestinations.APP_LIST) {
                        popUpTo(AppDestinations.APP_LIST)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
