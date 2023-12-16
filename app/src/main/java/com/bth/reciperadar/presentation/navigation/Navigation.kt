package com.bth.reciperadar.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.input.pointer.pointerInput
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
import com.bth.reciperadar.domain.controllers.CuisineController
import com.bth.reciperadar.domain.controllers.DietaryInfoController
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.screens.profilescreens.ProfileScreen
import com.bth.reciperadar.presentation.screens.mainscreen.MainScreen
import com.bth.reciperadar.presentation.screens.profilescreens.EditProfileScreen
import com.bth.reciperadar.presentation.screens.recipe.RecipeDetailScreen
import com.bth.reciperadar.presentation.screens.recipe.RecipeSearchScreen
import com.bth.reciperadar.presentation.screens.screen.Screen
import linearGradient

@Composable
fun Navigation(
    authController: AuthController,
    recipeController: RecipeController,
    ingredientController: IngredientController,
    ingredientTypeController: IngredientTypeController,
    cuisineController: CuisineController,
    dietaryInfoController: DietaryInfoController,
    profileController: ProfileController
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val screens = listOf(
        Screen.MainScreen,
        Screen.ProfileScreen
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)),
                backgroundColor = MaterialTheme.colorScheme.surface
            ) {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                        // label = { Text(text = screen.label) },
                        selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true,
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
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            val currentIndex = screens.indexOfFirst { it.route == navBackStackEntry?.destination?.route }
                            val threshold = 100f

                            if (pan.x > threshold && currentIndex > 0) {
                                val previousScreen = screens[currentIndex - 1]
                                navController.navigate(previousScreen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else if (pan.x < -threshold && currentIndex < screens.size - 1) {
                                val nextScreen = screens[currentIndex + 1]
                                navController.navigate(nextScreen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
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
                composable(route = Screen.ProfileScreen.route) {
                    ProfileScreen(
                        navController = navController,
                        authController = authController,
                        profileController = profileController
                    )
                }
                composable(route = Screen.EditProfileScreen.route) {
                    EditProfileScreen(
                        navController = navController,
                        authController = authController,
                        profileController = profileController,
                        dietaryInfoController = dietaryInfoController
                    )
                }
                composable(
                    route = Screen.RecipeSearchScreen.route + "/{searchQuery}",
                    arguments = listOf(
                        navArgument("searchQuery") {
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = false
                        }
                    )
                ) { entry ->
                    RecipeSearchScreen(
                        searchQuery = entry.arguments?.getString("searchQuery")!!,
                        navController = navController,
                        recipeController = recipeController,
                        ingredientController = ingredientController,
                        ingredientTypeController = ingredientTypeController,
                        cuisineController = cuisineController,
                        dietaryInfoController = dietaryInfoController,
                        profileController = profileController
                    )
                }
                composable(
                    route = Screen.RecipeDetailScreen.route + "/{recipeId}",
                    arguments = listOf(
                        navArgument("recipeId") {
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = false
                        }
                    )
                ) { entry ->
                    RecipeDetailScreen(
                        recipeId = entry.arguments?.getString("recipeId")!!,
                        recipeController = recipeController
                    )
                }
            }
        }
    }
}