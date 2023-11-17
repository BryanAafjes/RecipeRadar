package com.bth.reciperadar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.loginscreen.LoginScreen
import com.bth.reciperadar.navigation.Navigation
import com.bth.reciperadar.ui.theme.RecipeRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeRadarTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authController = remember { AuthController(this) }

                    val authenticated: Boolean by authController.authenticated.collectAsState()

                    if (authenticated) {
                        Navigation(authController = authController)
                    } else {
                        LoginScreen(authController)
                    }
                }
            }
        }
    }
}