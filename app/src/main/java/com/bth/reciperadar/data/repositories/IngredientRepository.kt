package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.IngredientDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IngredientRepository(db: FirebaseFirestore) {
    private val ingredientsCollection = db.collection("ingredients")

    suspend fun getIngredientsForRecipe(document: DocumentSnapshot): List<IngredientDto> {
        val ingredients = ArrayList<IngredientDto>()
        val firestoreIngredientMaps = document.get("ingredients") as List<Map<String, Any>>
        firestoreIngredientMaps.forEach { ingredientMap ->
            val ingredientReference = ingredientMap["ingredient"] as DocumentReference
            val ingredientId = ingredientReference.id
            val ingredientDto: IngredientDto? = getIngredient(ingredientId)

            if(ingredientDto != null) {
                ingredientDto.id = ingredientId
                ingredientDto.amount = ingredientMap["amount"] as String
                ingredients.add(ingredientDto)
            }
        }

        return ingredients
    }
    suspend fun getIngredient(ingredientId: String): IngredientDto? {
        return try {
            val documentSnapshot = ingredientsCollection.document(ingredientId).get().await()

            documentSnapshot.toObject(IngredientDto::class.java)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }
}