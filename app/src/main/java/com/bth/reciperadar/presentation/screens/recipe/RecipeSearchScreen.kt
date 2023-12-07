package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RecipeSearchScreen(searchQuery: String, recipeController: RecipeController, ingredientController: IngredientController, ingredientTypeController: IngredientTypeController) {
    var recipes by remember { mutableStateOf<List<RecipeViewModel>>(emptyList()) }
    var ingredientTypes by remember { mutableStateOf<List<IngredientTypeViewModel>>(emptyList()) }
    val state = rememberScrollState()

    LaunchedEffect(searchQuery) {
        withContext(Dispatchers.IO) {
            val recipeModels = recipeController.searchRecipes(searchQuery)
            recipes = recipeModels.map { it.toViewModel() }

            val ingredientTypeModels = ingredientTypeController.getIngredientTypes()
            ingredientTypes = ingredientTypeModels.map { it.toViewModel() }

            ingredientTypes = ingredientTypes.map { ingredientType ->
                val ingredientModels = withContext(Dispatchers.IO) {
                    ingredientController.getIngredientsForIngredientType(ingredientType.id)
                }
                ingredientType.copy(ingredients = ingredientModels.map { it.toViewModel() })
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
            .verticalScroll(state)
    ) {
        Text(text = "Hello!")
        Spacer(modifier = Modifier.height(20.dp))
        Text("Filter ingredients:")
        Spacer(modifier = Modifier.height(20.dp))
        IngredientTypeListView(ingredientTypes = ingredientTypes)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Recipes:")
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        RecipeListView(recipes = recipes)
        Spacer(modifier = Modifier.height(20.dp))
    }
}