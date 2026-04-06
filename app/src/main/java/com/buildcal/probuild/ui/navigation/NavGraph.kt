package com.buildcal.probuild.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.*
import androidx.navigation.compose.*
import com.buildcal.probuild.ui.screens.calculators.*
import com.buildcal.probuild.ui.screens.calendar.CalendarScreen
import com.buildcal.probuild.ui.screens.dashboard.DashboardScreen
import com.buildcal.probuild.ui.screens.estimates.AddMaterialPriceScreen
import com.buildcal.probuild.ui.screens.estimates.EstimatesScreen
import com.buildcal.probuild.ui.screens.materials.MaterialDetailScreen
import com.buildcal.probuild.ui.screens.materials.MaterialsScreen
import com.buildcal.probuild.ui.screens.measurements.AddMeasurementScreen
import com.buildcal.probuild.ui.screens.measurements.MeasurementsScreen
import com.buildcal.probuild.ui.screens.more.MoreScreen
import com.buildcal.probuild.ui.screens.notifications.NotificationsScreen
import com.buildcal.probuild.ui.screens.onboarding.OnboardingScreen
import com.buildcal.probuild.ui.screens.profile.ProfileScreen
import com.buildcal.probuild.ui.screens.projects.AddProjectScreen
import com.buildcal.probuild.ui.screens.projects.ProjectDetailScreen
import com.buildcal.probuild.ui.screens.projects.ProjectsScreen
import com.buildcal.probuild.ui.screens.reports.ReportsScreen
import com.buildcal.probuild.ui.screens.rooms.AddRoomScreen
import com.buildcal.probuild.ui.screens.rooms.RoomsScreen
import com.buildcal.probuild.ui.screens.settings.SettingsScreen
import com.buildcal.probuild.ui.screens.shopping.ShoppingListScreen
import com.buildcal.probuild.ui.screens.splash.SplashScreen
import com.buildcal.probuild.ui.screens.tasks.AddTaskScreen
import com.buildcal.probuild.ui.screens.tasks.TasksScreen

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard.route, Icons.Filled.Home, "Home"),
    BottomNavItem(Screen.Projects.route, Icons.Filled.Folder, "Projects"),
    BottomNavItem(Screen.Calculators.route, Icons.Filled.Calculate, "Calc"),
    BottomNavItem(Screen.Shopping.route, Icons.Filled.ShoppingCart, "Shop"),
    BottomNavItem(Screen.More.route, Icons.Filled.GridView, "More")
)

private val mainRoutes = setOf(
    Screen.Dashboard.route,
    Screen.Projects.route,
    Screen.Calculators.route,
    Screen.Shopping.route,
    Screen.More.route
)

private val noBarRoutes = setOf(
    Screen.Splash.route,
    Screen.Onboarding.route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val showBottomBar = currentRoute in mainRoutes
    val showNoBar = currentRoute in noBarRoutes
    val showTopBar = !showNoBar && currentRoute !in mainRoutes

    val screenTitle = when (currentRoute) {
        Screen.AddProject.route -> "New Project"
        Screen.Calculators.route -> "Calculators"
        Screen.BrickCalculator.route -> "Brick Calculator"
        Screen.ConcreteCalculator.route -> "Concrete Calculator"
        Screen.TileCalculator.route -> "Tile Calculator"
        Screen.PaintCalculator.route -> "Paint Calculator"
        Screen.DrywallCalculator.route -> "Drywall Calculator"
        Screen.InsulationCalculator.route -> "Insulation Calculator"
        Screen.Materials.route -> "Materials"
        Screen.AddMaterial.route -> "Add Material"
        Screen.Estimates.route -> "Cost Estimate"
        Screen.Measurements.route -> "Measurements"
        Screen.AddMeasurement.route -> "Add Measurement"
        Screen.Reports.route -> "Reports"
        Screen.Calendar.route -> "Calendar"
        Screen.Tasks.route -> "Tasks"
        Screen.AddTask.route -> "New Task"
        Screen.Notifications.route -> "Notifications"
        Screen.ActivityHistory.route -> "Activity History"
        Screen.Profile.route -> "Profile"
        Screen.Settings.route -> "Settings"
        else -> when {
            currentRoute?.startsWith("project_detail/") == true -> "Project Details"
            currentRoute?.startsWith("rooms/") == true -> "Rooms"
            currentRoute?.startsWith("add_room/") == true -> "Add Room"
            currentRoute?.startsWith("material_detail/") == true -> "Material Details"
            currentRoute?.startsWith("add_material_price/") == true -> "Update Price"
            else -> "Build Calc Pro"
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(screenTitle, style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    windowInsets = TopAppBarDefaults.windowInsets,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = {
                                Text(
                                    item.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute == item.route) return@NavigationBarItem
                                if (item.route == Screen.Dashboard.route) {
                                    if (!navController.popBackStack(
                                            Screen.Dashboard.route,
                                            inclusive = false
                                        )
                                    ) {
                                        navController.navigate(Screen.Dashboard.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Dashboard.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(navController)
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(Screen.Projects.route) {
                ProjectsScreen(navController)
            }
            composable(Screen.AddProject.route) {
                AddProjectScreen(navController)
            }
            composable(
                route = Screen.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
                ProjectDetailScreen(navController, projectId)
            }
            composable(
                route = Screen.Rooms.route,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
                RoomsScreen(navController, projectId)
            }
            composable(
                route = Screen.AddRoom.route,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
                AddRoomScreen(navController, projectId)
            }
            composable(Screen.Calculators.route) {
                CalculatorsScreen(navController)
            }
            composable(Screen.BrickCalculator.route) {
                BrickCalculatorScreen(navController)
            }
            composable(Screen.ConcreteCalculator.route) {
                ConcreteCalculatorScreen(navController)
            }
            composable(Screen.TileCalculator.route) {
                TileCalculatorScreen(navController)
            }
            composable(Screen.PaintCalculator.route) {
                PaintCalculatorScreen(navController)
            }
            composable(Screen.DrywallCalculator.route) {
                DrywallCalculatorScreen(navController)
            }
            composable(Screen.InsulationCalculator.route) {
                InsulationCalculatorScreen(navController)
            }
            composable(Screen.Shopping.route) {
                ShoppingListScreen(navController)
            }
            composable(Screen.More.route) {
                MoreScreen(navController)
            }
            composable(Screen.Materials.route) {
                MaterialsScreen(navController)
            }
            composable(
                route = Screen.MaterialDetail.route,
                arguments = listOf(navArgument("materialId") { type = NavType.LongType })
            ) { backStackEntry ->
                val materialId = backStackEntry.arguments?.getLong("materialId") ?: 0L
                MaterialDetailScreen(navController, materialId)
            }
            composable(Screen.AddMaterial.route) {
                com.buildcal.probuild.ui.screens.materials.AddMaterialScreen(navController)
            }
            composable(Screen.Estimates.route) {
                EstimatesScreen(navController)
            }
            composable(
                route = Screen.AddMaterialPrice.route,
                arguments = listOf(navArgument("materialId") { type = NavType.LongType })
            ) { backStackEntry ->
                val materialId = backStackEntry.arguments?.getLong("materialId") ?: 0L
                AddMaterialPriceScreen(navController, materialId)
            }
            composable(Screen.Measurements.route) {
                MeasurementsScreen(navController)
            }
            composable(Screen.AddMeasurement.route) {
                AddMeasurementScreen(navController)
            }
            composable(Screen.Reports.route) {
                ReportsScreen(navController)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(navController)
            }
            composable(Screen.Tasks.route) {
                TasksScreen(navController)
            }
            composable(Screen.AddTask.route) {
                AddTaskScreen(navController)
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen(navController)
            }
            composable(Screen.ActivityHistory.route) {
                com.buildcal.probuild.ui.screens.history.ActivityHistoryScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
        }
    }
}
