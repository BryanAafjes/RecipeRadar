package com.bth.reciperadar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bth.reciperadar.data.repositories.RecipeRepository
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.screens.loginscreen.StartScreen
import com.bth.reciperadar.presentation.navigation.Navigation
import com.bth.reciperadar.ui.theme.RecipeRadarTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                    // Authentication instances
                    val authController = remember { AuthController(this) }
                    val currentUser = authController.currentUser.collectAsState()

                    // Data/Repository instances
                    val db = Firebase.firestore

                    val recipeRepository = remember { RecipeRepository(db) }

                    // Controller instances
                    val recipeController = remember { RecipeController(recipeRepository)}

                    if (currentUser.value != null) {
                        Navigation(
                            authController = authController,
                            recipeController = recipeController
                        )
                    } else {
                        StartScreen(authController)
                    }
                }
            }
        }
    }
}