package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeRepository(db: FirebaseFirestore) {
    private val recipesCollection = db.collection("recipes")
    private val ingredientRepository = IngredientRepository(db)

    private suspend fun getRecipes(includeReferences: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = recipesCollection.get().await()
            val recipesList = mutableListOf<RecipeDto>()

            for (document in querySnapshot.documents) {
                val recipe = document.toObject(RecipeDto::class.java)
                recipe?.id = document.id

                if (includeReferences) {
                    recipe?.ingredients = ingredientRepository.getIngredientsForRecipe(document)
                }

                recipe?.let {
                    recipesList.add(it)
                }
            }

            return recipesList

        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRecipesWithoutReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = false)
    }

    suspend fun getRecipesWithReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = true)
    }
}