package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    recipeController: RecipeController
) {
    var recipe by remember { mutableStateOf<RecipeViewModel?>(null) }

    LaunchedEffect(recipeId) {
        withContext(Dispatchers.IO) {
            val recipeModel = recipeController.getRecipeById(recipeId)
            recipe = recipeModel?.toViewModel()
        }
    }

    recipe?.let { Text(it.title) }
}