package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.ShoppingListRepository
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.ShoppingList
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingListController(
    private val authController: AuthController,
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend fun getShoppingList(): ShoppingList? {
        return withContext(Dispatchers.IO) {
            val userId = authController.getCurrentUserId()

            if (userId != null) {
                try {
                    return@withContext shoppingListRepository.getShoppingListByUserId(userId)?.toDomain()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            null
        }
    }

    suspend fun createOrUpdateShoppingList(shoppingList: ShoppingList): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authController.getCurrentUserId()

                if (userId != null) {
                    shoppingList.userId = userId
                    return@withContext shoppingListRepository.createOrUpdateShoppingList(shoppingList.toDto())
                }

                return@withContext false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun addIngredientListToShoppingList(ingredients: List<Ingredient>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val shoppingList = getShoppingList()

                if (shoppingList != null){
                    shoppingList.ingredients = shoppingList.ingredients.plus(ingredients)

                    createOrUpdateShoppingList(shoppingList = shoppingList)
                    return@withContext true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }
    }
}