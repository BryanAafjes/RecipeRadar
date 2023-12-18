package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.InventoryDto
import com.bth.reciperadar.data.dtos.ShoppingListDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class ShoppingListRepository(db: FirebaseFirestore) {
    private val shoppingListCollection = db.collection("shopping_lists")
    private val ingredientCollection = db.collection("ingredients")

    private val ingredientRepository = IngredientRepository(db)

    suspend fun getShoppingListByUserId(userId: String): ShoppingListDto? {
        return try {
            val querySnapshot = shoppingListCollection
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return null
            }

            val documentSnapshot = querySnapshot.first()
            val shoppingListDto = documentSnapshot.toObject(ShoppingListDto::class.java)

            shoppingListDto.id = documentSnapshot.id
            shoppingListDto.userId = documentSnapshot.get("user_id")?.toString()!!

            shoppingListDto.ingredients = ingredientRepository.getIngredientsForReferences(documentSnapshot, "ingredient_references")
            shoppingListDto.checkedIngredients = ingredientRepository.getIngredientsForReferences(documentSnapshot, "checked_ingredient_references")

            return shoppingListDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    suspend fun createOrUpdateShoppingList(shoppingListDto: ShoppingListDto): Boolean {
        return try {
            shoppingListDto.id = shoppingListDto.id.ifEmpty {
                shoppingListCollection.document().id
            }

            shoppingListCollection.document(shoppingListDto.id)
                .set(shoppingListDto.toFirebaseMap(), SetOptions.merge())
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun ShoppingListDto.toFirebaseMap(): Map<String, Any?> {
        val shoppingListIngredientReferences = ingredients.map { it.id }.map {
            ingredientCollection.document(it)
        } ?: emptyList<DocumentReference>()

        val shoppingListCheckedIngredientReferences = checkedIngredients.map { it.id }.map {
            ingredientCollection.document(it)
        } ?: emptyList<DocumentReference>()

        return mapOf(
            "user_id" to userId,
            "ingredient_references" to shoppingListIngredientReferences,
            "checked_ingredient_references" to shoppingListCheckedIngredientReferences,
        )
    }
}