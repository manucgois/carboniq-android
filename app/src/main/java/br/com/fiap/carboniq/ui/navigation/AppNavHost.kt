package br.com.fiap.carboniq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.carboniq.ui.CarbonViewModel
import br.com.fiap.carboniq.ui.screens.HistoryItem
import br.com.fiap.carboniq.ui.screens.HistoryScreen
import br.com.fiap.carboniq.ui.screens.HomeScreen
import br.com.fiap.carboniq.ui.screens.NewSimulationScreen
import br.com.fiap.carboniq.ui.screens.ResultScreen
import br.com.fiap.carboniq.ui.screens.TipsScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {

    val carbonViewModel: CarbonViewModel = viewModel()
    val totalCo2 by carbonViewModel.totalCo2.collectAsState()
    val history by carbonViewModel.history.collectAsState()
    val airQuality by carbonViewModel.airQuality.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                totalCo2 = totalCo2,
                airQuality = airQuality,
                onRetryAirQuality = { carbonViewModel.retryAirQuality() },
                onNewSimulationClick = { navController.navigate(Screen.NewSimulation.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onTipsClick = { navController.navigate(Screen.Tips.route) }
            )
        }

        composable(Screen.NewSimulation.route) {
            NewSimulationScreen(
                onResultReady = { co2Value, activityType ->
                    navController.navigate(Screen.Result.createRoute(co2Value, activityType))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("co2Value") { type = NavType.FloatType },
                navArgument("activityType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val co2Value = backStackEntry.arguments?.getFloat("co2Value") ?: 0f
            val activityType = backStackEntry.arguments?.getString("activityType") ?: ""

            ResultScreen(
                co2Value = co2Value,
                activityType = activityType,
                onSeeHistory = {
                    carbonViewModel.saveSimulation(activityType, co2Value)
                    navController.navigate(Screen.History.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onSeeTips = { navController.navigate(Screen.Tips.route) },
                onBackToHome = { navController.popBackStack(Screen.Home.route, inclusive = false) }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                items = history.map { entry ->
                    HistoryItem(
                        activityType = entry.activityType,
                        co2Value = entry.co2Value,
                        timestamp = entry.timestamp
                    )
                }
            )
        }

        composable(Screen.Tips.route) {
            TipsScreen(onBack = { navController.popBackStack() })
        }
    }
}