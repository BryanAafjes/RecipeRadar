package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.InventoryRepository
import com.bth.reciperadar.domain.models.Ingredient
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
            val userId = authController.getCurrentUserId()

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

    suspend fun createOrUpdateInventory(inventory: Inventory): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authController.getCurrentUserId()

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

    suspend fun addIngredientListToInventory(ingredients: List<Ingredient>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val inventory = getInventory()

                if (inventory != null){
                    inventory.ingredients = inventory.ingredients.plus(ingredients)

                    createOrUpdateInventory(inventory = inventory)
                    return@withContext true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }
    }

    suspend fun removeIngredientListFromInventory(ingredients: List<Ingredient>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val inventory = getInventory()

                if (inventory != null){
                    inventory.ingredients = inventory.ingredients.filterNot { inventoryIngredient ->
                        ingredients.any { ingredientToRemove -> inventoryIngredient.id == ingredientToRemove.id }
                    }

                    createOrUpdateInventory(inventory = inventory)
                    return@withContext true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }
    }
}