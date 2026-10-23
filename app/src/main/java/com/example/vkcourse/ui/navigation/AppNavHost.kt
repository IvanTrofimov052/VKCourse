package com.example.vkcourse.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkcourse.data.AppCatalog
import com.example.vkcourse.ui.appdetails.AppDetailsScreen
import com.example.vkcourse.ui.applist.AppListRoute

private const val APP_LIST = "apps"
private const val APP_ID = "appId"
private const val APP_DETAILS = "app/{$APP_ID}"

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = APP_LIST,
        modifier = modifier,
    ) {
        composable(APP_LIST) {
            AppListRoute(
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
            val app = requireNotNull(AppCatalog.findApp(requireNotNull(entry.arguments?.getString(APP_ID))))
            AppDetailsScreen(
                app = app,
                showcaseApps = AppCatalog.showcaseApps,
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
