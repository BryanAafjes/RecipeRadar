package com.bth.reciperadar.presentation.screens.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object MainScreen : Screen("main_screen", Icons.Default.Home, "MainScreen")
    object DetailScreen : Screen("detail_screen", Icons.Default.List, "DetailScreen")
    object StorageScreen : Screen("storage_screen", Icons.Default.List, "StorageScreen" )
    object ListScreen : Screen("list_screen", Icons.Default.Check, "ListScreen")
    object ProfileScreen : Screen("profile_screen", Icons.Default.Person, "ProfileScreen")
    object EditProfileScreen : Screen("edit_profile_screen", Icons.Default.Person, "EditProfileScreen")
    object RecipeSearchScreen: Screen("recipe_search_screen", Icons.Default.Search, "RecipeSearchScreen")
    object RecipeDetailScreen: Screen("recipe_detail_screen", Icons.Default.Info, "RecipeDetailScreen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }

    fun withArgs(args: Map<String, Any?>): String {
        return buildString {
            append(route)
            args.forEach { (argName, argValue) ->
                append("/$argValue")
            }
        }
    }

}