package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.InventoryRepository
import com.bth.reciperadar.domain.models.Inventory
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InventoryController(
    private val authController: AuthController,
    private val inventoryRepository: InventoryRepository
) {
    suspend fun getInventory(): Inventory? {
        return withContext(Dispatchers.IO) {
            val userId = authController.auth.currentUser?.uid

            if (userId != null) {
                try {
                    return@withContext inventoryRepository.getInventoryByUserId(userId)?.toDomain()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            null
        }
    }

    suspend fun createOrUpdateProfile(inventory: Inventory): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authController.auth.currentUser?.uid

                if (userId != null) {
                    inventory.userId = userId
                    return@withContext inventoryRepository.createOrUpdateInventory(inventory.toDto())
                }

                return@withContext false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}