package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.CuisineRepository
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CuisineController(private val cuisineRepository: CuisineRepository) {
    suspend fun getCuisines(): List<Cuisine> = withContext(Dispatchers.IO) {
        try {
            val cuisineDtoList = cuisineRepository.getCuisines()
            return@withContext cuisineDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}