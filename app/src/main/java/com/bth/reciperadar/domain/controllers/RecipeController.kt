package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.RecipeRepository
import com.bth.reciperadar.domain.models.Recipe
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeController(private val recipeRepository: RecipeRepository) {
    suspend fun getRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val recipeDtoList = recipeRepository.getRecipes()
            return@withContext recipeDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}