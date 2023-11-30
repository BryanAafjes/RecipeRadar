package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeRepository(val db: FirebaseFirestore) {
    private val recipesCollection = db.collection("recipes")

    suspend fun getRecipes(): List<RecipeDto> {
        return try {
            val querySnapshot = recipesCollection.get().await()
            val recipesList = mutableListOf<RecipeDto>()

            for (document in querySnapshot.documents) {
                val recipe = document.toObject(RecipeDto::class.java)
                recipe?.let { recipesList.add(it) }
            }

            return recipesList

        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}