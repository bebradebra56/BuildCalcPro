package com.buildcal.probuild.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Projects : Screen("projects")
    object AddProject : Screen("add_project")
    object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: Long) = "project_detail/$projectId"
    }
    object Rooms : Screen("rooms/{projectId}") {
        fun createRoute(projectId: Long) = "rooms/$projectId"
    }
    object AddRoom : Screen("add_room/{projectId}") {
        fun createRoute(projectId: Long) = "add_room/$projectId"
    }
    object Calculators : Screen("calculators")
    object BrickCalculator : Screen("brick_calculator")
    object ConcreteCalculator : Screen("concrete_calculator")
    object TileCalculator : Screen("tile_calculator")
    object PaintCalculator : Screen("paint_calculator")
    object DrywallCalculator : Screen("drywall_calculator")
    object InsulationCalculator : Screen("insulation_calculator")
    object Shopping : Screen("shopping")
    object More : Screen("more")
    object Materials : Screen("materials")
    object MaterialDetail : Screen("material_detail/{materialId}") {
        fun createRoute(materialId: Long) = "material_detail/$materialId"
    }
    object AddMaterial : Screen("add_material")
    object Estimates : Screen("estimates")
    object AddMaterialPrice : Screen("add_material_price/{materialId}") {
        fun createRoute(materialId: Long) = "add_material_price/$materialId"
    }
    object Measurements : Screen("measurements")
    object AddMeasurement : Screen("add_measurement")
    object Reports : Screen("reports")
    object Calendar : Screen("calendar")
    object Tasks : Screen("tasks")
    object AddTask : Screen("add_task")
    object Notifications : Screen("notifications")
    object ActivityHistory : Screen("activity_history")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
