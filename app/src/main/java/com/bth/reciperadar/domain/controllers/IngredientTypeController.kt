package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.IngredientTypeRepository
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IngredientTypeController(private val ingredientTypeRepository: IngredientTypeRepository) {
    suspend fun getIngredientTypes(): List<IngredientType> = withContext(Dispatchers.IO) {
        try {
            val ingredientTypeDtoList = ingredientTypeRepository.getIngredientTypes()
            return@withContext ingredientTypeDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}