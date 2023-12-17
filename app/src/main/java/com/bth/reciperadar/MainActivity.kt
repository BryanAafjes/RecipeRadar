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
import com.bth.reciperadar.data.repositories.CuisineRepository
import com.bth.reciperadar.data.repositories.DietaryInfoRepository
import com.bth.reciperadar.data.repositories.IngredientRepository
import com.bth.reciperadar.data.repositories.IngredientTypeRepository
import com.bth.reciperadar.data.repositories.InventoryRepository
import com.bth.reciperadar.data.repositories.ProfileRepository
import com.bth.reciperadar.data.repositories.RecipeRepository
import com.bth.reciperadar.data.repositories.ShoppingListRepository
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.CuisineController
import com.bth.reciperadar.domain.controllers.DietaryInfoController
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.domain.controllers.ShoppingListController
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
                    val ingredientRepository = remember { IngredientRepository(db) }
                    val ingredientTypeRepository = remember { IngredientTypeRepository(db) }
                    val cuisineRepository = remember { CuisineRepository(db) }
                    val dietaryInfoRepository = remember { DietaryInfoRepository(db) }
                    val profileRepository = remember { ProfileRepository(db) }
                    val shoppingListRepository = remember { ShoppingListRepository(db) }
                    val inventoryRepository = remember { InventoryRepository(db) }

                    // Controller instances
                    val recipeController = remember { RecipeController(recipeRepository) }
                    val ingredientController = remember { IngredientController(ingredientRepository) }
                    val ingredientTypeController = remember { IngredientTypeController(ingredientTypeRepository) }
                    val cuisineController = remember { CuisineController(cuisineRepository) }
                    val dietaryInfoController = remember { DietaryInfoController(dietaryInfoRepository) }
                    val profileController = remember { ProfileController(authController, profileRepository) }
                    val shoppingListController = remember { ShoppingListController(authController, shoppingListRepository) }
                    val inventoryController = remember { InventoryController(authController, inventoryRepository) }

                    if (currentUser.value != null) {
                        Navigation(
                            authController = authController,
                            recipeController = recipeController,
                            ingredientController = ingredientController,
                            ingredientTypeController = ingredientTypeController,
                            cuisineController = cuisineController,
                            dietaryInfoController = dietaryInfoController,
                            profileController = profileController,
                            shoppingListController = shoppingListController,
                            inventoryController = inventoryController
                        )
                    } else {
                        StartScreen(authController)
                    }
                }
            }
        }
    }
}