package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.InventoryDto
import com.bth.reciperadar.data.dtos.ProfileDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class InventoryRepository(db: FirebaseFirestore) {
    private val inventoryCollection = db.collection("inventories")
    private val ingredientCollection = db.collection("ingredients")

    private val ingredientRepository = IngredientRepository(db)

    suspend fun getInventoryByUserId(userId: String): InventoryDto? {
        return try {
            val querySnapshot = inventoryCollection
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return null
            }

            val documentSnapshot = querySnapshot.first()
            val inventoryDto = documentSnapshot.toObject(InventoryDto::class.java)

            inventoryDto.id = documentSnapshot.id
            inventoryDto.userId = documentSnapshot.get("user_id")?.toString()!!

            inventoryDto.ingredients = ingredientRepository.getIngredientsForReferences(documentSnapshot, "ingredient_references")

            return inventoryDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    suspend fun createOrUpdateInventory(inventoryDto: InventoryDto): Boolean {
        return try {
            inventoryDto.id = inventoryDto.id.ifEmpty {
                inventoryCollection.document().id
            }

            inventoryCollection.document(inventoryDto.id)
                .set(inventoryDto.toFirebaseMap(), SetOptions.merge())
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun InventoryDto.toFirebaseMap(): Map<String, Any?> {
        val inventoryIngredientReferences = ingredients.map { it.id }.map {
            ingredientCollection.document(it)
        } ?: emptyList<DocumentReference>()

        return mapOf(
            "user_id" to userId,
            "ingredient_references" to inventoryIngredientReferences,
        )
    }
}