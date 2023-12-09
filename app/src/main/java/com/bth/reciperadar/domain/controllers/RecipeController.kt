package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.repositories.RecipeRepository
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.Recipe
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeController(private val recipeRepository: RecipeRepository) {
    suspend fun getRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val recipeDtoList = recipeRepository.getRecipesWithoutReferences()
            return@withContext recipeDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRecipeById(recipeId: String): Recipe? = withContext(Dispatchers.IO) {
        try {
            val recipeDto = recipeRepository.getRecipeById(recipeId, includeIngredients = true, includeReferences = true)
            return@withContext recipeDto?.toDomain()
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            return@withContext null
        }
    }

    suspend fun searchRecipes(searchQuery: String): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val searchQueryLowercase = searchQuery.lowercase()
            val searchWords = searchQueryLowercase.split(" ")

            val recipeDtoList = recipeRepository.searchRecipesByTitle(searchWords, false)
            return@withContext recipeDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchRecipesByTitleAndIngredientFilter(
        searchQuery: String,
        ingredientsList: List<Ingredient>,
        anyRecipesWithSelectedIngredients: Boolean,
        dontAllowExtraIngredients: Boolean
    ): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val searchQueryLowercase = searchQuery.lowercase()
            val searchWords = searchQueryLowercase.split(" ")

            var recipeList = recipeRepository.searchRecipesByTitle(searchWords, true).map { it.toDomain() }

            if (ingredientsList.isNotEmpty()) {
                recipeList = if (anyRecipesWithSelectedIngredients) {
                    // Any with selected ingredients
                    recipeList.filter { recipe ->
                        ingredientsList.any { ingredient ->
                            recipe.ingredients?.any { it.id == ingredient.id } == true
                        }
                    }
                } else if (dontAllowExtraIngredients) {
                    // Don't allow optional ingredients
                    recipeList.filter { recipe ->
                        val recipeIngredientIds = recipe.ingredients?.map { it.id } ?: emptyList()
                        ingredientsList.map { it.id }.containsAll(recipeIngredientIds)
                    }
                } else {
                    // Allow optional ingredients
                    recipeList.filter { recipe ->
                        ingredientsList.all { ingredient ->
                            recipe.ingredients?.any { it.id == ingredient.id } == true
                        }
                    }
                }
            }

            return@withContext recipeList
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}