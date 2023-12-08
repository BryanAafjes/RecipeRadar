package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class RecipeRepository(db: FirebaseFirestore) {
    private val recipesCollection = db.collection("recipes")
    private val ingredientCollection = db.collection("ingredients")
    private val ingredientRepository = IngredientRepository(db)
    private val reviewRepository = ReviewRepository(db)
    private val dietaryInfoRepository = DietaryInfoRepository(db)
    private val cuisineRepository = CuisineRepository(db)

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
                    recipe.dietaryInfo = dietaryInfoRepository.getDietaryInfoForRecipe(document)
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

    suspend fun searchRecipesByTitle(lowercaseSearchWords: List<String>, includeIngredients: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = recipesCollection
                .whereArrayContainsAny("search_title", lowercaseSearchWords)
                .get()
                .await()

            return getRecipesFromQueryDocuments(querySnapshot.documents, includeIngredients, false)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchRecipesByTitleAndIngredientFilter(
        lowercaseSearchWords: List<String>,
        ingredientsList: List<IngredientDto>,
        recipesShouldContainAllSelectedIngredients: Boolean,
        recipesWithOnlySelectedIngredients: Boolean
    ): List<RecipeDto> {
        var recipeList = searchRecipesByTitle(lowercaseSearchWords, true)

        if (ingredientsList.isNotEmpty()) {
            recipeList = if (recipesShouldContainAllSelectedIngredients) {
                recipeList.filter { recipe ->
                    ingredientsList.all { ingredient ->
                        recipe.ingredients?.any { it.id == ingredient.id } == true
                    }
                }
            } else if (recipesWithOnlySelectedIngredients) {
                recipeList.filter { recipe ->
                    val recipeIngredientIds = recipe.ingredients?.map { it.id } ?: emptyList()
                    ingredientsList.map { it.id }.containsAll(recipeIngredientIds)
                }
            } else {
                recipeList.filter { recipe ->
                    ingredientsList.any { ingredient ->
                        recipe.ingredients?.any { it.id == ingredient.id } == true
                    }
                }
            }
        }

        return recipeList
    }
}