package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeRepository(db: FirebaseFirestore) {
    private val recipesCollection = db.collection("recipes")
    private val ingredientCollection = db.collection("ingredients")
    private val ingredientRepository = IngredientRepository(db)
    private val reviewRepository = ReviewRepository(db)
    private val dietaryInfoRepository = DietaryInfoRepository(db)
    private val cuisineRepository = CuisineRepository(db)

    suspend fun getRecipeById(recipeId: String, includeIngredients: Boolean, includeReferences: Boolean): RecipeDto? {
        return try {
            val documentSnapshot = recipesCollection.document(recipeId).get().await()
            val recipe = documentSnapshot.toObject(RecipeDto::class.java)

            if (recipe != null) {
                recipe.id = documentSnapshot.id
                recipe.prepTime = documentSnapshot.get("prep_time")?.toString()
                recipe.picturePath = documentSnapshot.get("picture_path")?.toString()
                recipe.userId = documentSnapshot.get("user_id")?.toString()

                val servingAmount = documentSnapshot.get("serving_amount") as Long
                recipe.servingAmount = servingAmount.toInt()

                if (includeIngredients) {
                    recipe.ingredients = ingredientRepository.getIngredientsForRecipe(documentSnapshot)
                }
                if (includeReferences) {
                    recipe.reviews = reviewRepository.getReviewsForRecipe(recipe.id)
                    recipe.dietaryInfo =
                        dietaryInfoRepository.getDietaryInfoForReferences(documentSnapshot)
                    recipe.cuisines = cuisineRepository.getDietaryInfoForRecipe(documentSnapshot)
                }
            }

            return recipe
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }


    private suspend fun getRecipes(includeReferences: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = recipesCollection.get().await()
            return getRecipesFromQueryDocuments(querySnapshot.documents, includeReferences, includeReferences)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun getRecipesFromQueryDocuments(documents: List<DocumentSnapshot>, includeIngredients: Boolean, includeReferences: Boolean): List<RecipeDto> {
        val recipesList = ArrayList<RecipeDto>()

        for (document in documents) {
            val recipe = document.toObject(RecipeDto::class.java)

            if (recipe != null) {
                recipe.id = document.id
                recipe.prepTime = document.get("prep_time")?.toString()
                recipe.picturePath = document.get("picture_path")?.toString()
                recipe.userId = document.get("user_id")?.toString()

                val servingAmount = document.get("serving_amount") as Long
                recipe.servingAmount = servingAmount.toInt()

                if (includeIngredients) {
                    recipe.ingredients = ingredientRepository.getIngredientsForRecipe(document)
                }
                if (includeReferences) {
                    recipe.reviews = reviewRepository.getReviewsForRecipe(recipe.id)
                    recipe.dietaryInfo = dietaryInfoRepository.getDietaryInfoForReferences(document)
                    recipe.cuisines = cuisineRepository.getDietaryInfoForRecipe(document)
                }

                recipe.let {
                    recipesList.add(it)
                }
            }
        }
        return recipesList
    }

    suspend fun getRecipesWithoutReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = false)
    }

    suspend fun getRecipesWithReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = true)
    }

    suspend fun searchRecipesByTitle(lowercaseSearchWords: List<String>?, includeIngredients: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = lowercaseSearchWords?.let {
                recipesCollection.whereArrayContainsAny("search_title", it).get().await()
            } ?: recipesCollection.get().await()

            return getRecipesFromQueryDocuments(querySnapshot.documents, includeIngredients, true)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}