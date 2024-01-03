package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.IngredientRepository
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IngredientController(private val ingredientRepository: IngredientRepository) {
    suspend fun getIngredients(): List<Ingredient> = withContext(Dispatchers.IO) {
        try {
            val ingredientDtoList = ingredientRepository.getIngredients()
            return@withContext ingredientDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getIngredientsForIngredientType(ingredientTypeId: String): List<Ingredient> = withContext(Dispatchers.IO) {
        try {
            val ingredientDtoList = ingredientRepository.getIngredientsForIngredientType(ingredientTypeId)
            return@withContext ingredientDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchIngredientsByName(searchQuery: String): Ingredient? = withContext(Dispatchers.IO)
    {
        try {
            val searchQueryLowercase = searchQuery.lowercase()
            val searchWords = searchQueryLowercase.split(" ")

            val ingredientList = ingredientRepository.searchIngredientsByTitle(searchWords)
                .map { it.toDomain() }

            val matchingIngredients = ingredientList.map { ingredient ->
                val ingredientSearchWords = ingredient.name.lowercase().split(" ")
                val userSearchWords = searchWords.toSet()

                val commonSearchWords = userSearchWords.intersect(ingredientSearchWords.toSet())
                Pair(ingredient, commonSearchWords.size)
            }

            val sortedIngredients = matchingIngredients.sortedByDescending { it.second }

            return@withContext sortedIngredients.firstOrNull()?.first
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            null
        }
    }
}