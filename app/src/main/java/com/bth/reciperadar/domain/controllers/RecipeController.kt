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

            val recipeDtoList = recipeRepository.searchRecipesByTitleAndIngredientFilter(
                searchWords,
                ingredientsList.map { it.toDto() },
                anyRecipesWithSelectedIngredients = anyRecipesWithSelectedIngredients,
                dontAllowExtraIngredients = dontAllowExtraIngredients
            )
            return@withContext recipeDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}