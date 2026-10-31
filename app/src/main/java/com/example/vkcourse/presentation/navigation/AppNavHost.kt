package com.example.vkcourse.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkcourse.domain.repository.AppRepository
import com.example.vkcourse.presentation.appdetails.AppDetailsRoute
import com.example.vkcourse.presentation.applist.AppListRoute

private const val APP_LIST = "apps"
private const val APP_ID = "appId"
private const val APP_DETAILS = "app/{$APP_ID}"

@Composable
fun AppNavHost(repository: AppRepository, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = APP_LIST,
        modifier = modifier,
    ) {
        composable(APP_LIST) {
            AppListRoute(
                repository = repository,
                onAppClick = { app ->
                    navController.navigate("app/${Uri.encode(app.id)}") {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(
            route = APP_DETAILS,
            arguments = listOf(navArgument(APP_ID) { type = NavType.StringType }),
        ) { entry ->
            AppDetailsRoute(
                appId = requireNotNull(entry.arguments?.getString(APP_ID)),
                repository = repository,
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = {
                    navController.navigate(APP_LIST) {
                        popUpTo(APP_LIST)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
