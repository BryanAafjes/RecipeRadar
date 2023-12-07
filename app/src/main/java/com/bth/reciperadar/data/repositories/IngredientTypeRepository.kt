package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.IngredientTypeDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IngredientTypeRepository(db: FirebaseFirestore) {
    private val ingredientTypesCollection = db.collection("ingredient_types")

    suspend fun getIngredientType(ingredientTypeId: String): IngredientTypeDto? {
        return try {
            val documentSnapshot = ingredientTypesCollection.document(ingredientTypeId).get().await()

            val ingredientTypeDto = documentSnapshot.toObject(IngredientTypeDto::class.java)

            ingredientTypeDto?.id = documentSnapshot.id

            return ingredientTypeDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    private fun getIngredientTypesFromQueryDocuments(documents: List<DocumentSnapshot>): List<IngredientTypeDto> {
        val ingredientTypes = ArrayList<IngredientTypeDto>()

        for (document in documents) {
            val ingredientTypeDto = document.toObject(IngredientTypeDto::class.java)

            if (ingredientTypeDto != null) {
                ingredientTypeDto.id = document.id
                ingredientTypes.add(ingredientTypeDto)
            }
        }
        return ingredientTypes
    }

    suspend fun getIngredientTypes(): List<IngredientTypeDto> {
        return try {
            val querySnapshot = ingredientTypesCollection.get().await()
            return getIngredientTypesFromQueryDocuments(querySnapshot.documents)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}