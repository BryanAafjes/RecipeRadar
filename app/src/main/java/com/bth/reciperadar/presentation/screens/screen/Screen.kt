package com.bth.reciperadar.presentation.screens.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object MainScreen : Screen("main_screen", Icons.Default.Home, "MainScreen")
    object DetailScreen : Screen("detail_screen", Icons.Default.List, "DetailScreen")
    object AccountScreen : Screen("account_screen", Icons.Default.Person, "AccountScreen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}