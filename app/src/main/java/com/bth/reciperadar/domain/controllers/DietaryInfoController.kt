package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.DietaryInfoRepository
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DietaryInfoController(private val dietaryInfoRepository: DietaryInfoRepository) {
    suspend fun getDietaryInfo(): List<DietaryInfo> = withContext(Dispatchers.IO) {
        try {
            val dietaryInfoDtoList = dietaryInfoRepository.getDietaryInfoList()
            return@withContext dietaryInfoDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or repository errors
            e.printStackTrace()
            emptyList()
        }
    }
}