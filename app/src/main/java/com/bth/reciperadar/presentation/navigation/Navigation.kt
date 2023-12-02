package com.bth.reciperadar.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bth.reciperadar.presentation.screens.detailscreen.DetailScreen
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.mainscreen.AccountScreen
import com.bth.reciperadar.presentation.screens.listscreen.ListScreen
import com.bth.reciperadar.presentation.screens.mainscreen.MainScreen
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.screens.storagescreen.StorageScreen
import linearGradient

@Composable
fun Navigation(authController: AuthController, recipeController: RecipeController) {
    val navController = rememberNavController()

    val screens = listOf(
        Screen.MainScreen,
        Screen.ListScreen,
        Screen.StorageScreen,
        Screen.AccountScreen
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)),
                backgroundColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                        // label = { Text(text = screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        selectedContentColor = Color.Blue,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        // Create a radial gradient brush
        val gradientBrush = Brush.linearGradient(
            0.3f to Color(0x002C2B2B),
            0.5f to Color(0x8C4D4D4D),
            0.7f to Color(0x002C2B2B),
            angleInDegrees = 30f
        )

        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = Screen.MainScreen.route,
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(gradientBrush)
            ) {
                composable(route = Screen.MainScreen.route) {
                    MainScreen(navController = navController, authController = authController, recipeController = recipeController)
                }
                composable(
                    route = Screen.DetailScreen.route + "/{name}",
                    arguments = listOf(
                        navArgument("name") {
                            type = NavType.StringType
                            defaultValue = "Bryan"
                            nullable = false
                        }
                    )
                ) { entry ->
                    DetailScreen(name = entry.arguments?.getString("name"))
                }
                composable(route = Screen.AccountScreen.route) {
                    AccountScreen(authController = authController)
                }
                composable( route = Screen.ListScreen.route) { ListScreen() }
                composable( route = Screen.StorageScreen.route) { StorageScreen() }
            }
        }
    }
}