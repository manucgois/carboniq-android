package br.com.fiap.carboniq.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NewSimulation : Screen("new_simulation")
    object Result : Screen("result/{co2Value}/{activityType}") {
        fun createRoute(co2Value: Float, activityType: String) =
            "result/$co2Value/$activityType"
    }
    object History : Screen("history")
    object Tips : Screen("tips")
}