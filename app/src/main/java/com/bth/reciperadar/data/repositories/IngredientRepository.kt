package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IngredientRepository(db: FirebaseFirestore) {
    private val ingredientsCollection = db.collection("ingredients")
    private val ingredientTypesCollection = db.collection("ingredient_types")
    private val ingredientTypeRepository = IngredientTypeRepository(db)

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

    suspend fun getIngredientsForReferences(document: DocumentSnapshot, referenceName: String): List<IngredientDto> {
        val ingredientList = ArrayList<IngredientDto>()
        val firestoreIngredientReferences: List<DocumentReference> = document.get(referenceName) as List<DocumentReference>
        firestoreIngredientReferences.forEach { ingredientReference ->
            val ingredientId = ingredientReference.id
            val ingredientDto: IngredientDto? = getIngredient(ingredientId)

            if(ingredientDto != null) {
                ingredientDto.id = ingredientId
                ingredientList.add(ingredientDto)
            }
        }

        return ingredientList
    }

    suspend fun getIngredient(ingredientId: String): IngredientDto? {
        return try {
            val documentSnapshot = ingredientsCollection.document(ingredientId).get().await()

            val ingredientDto = documentSnapshot.toObject(IngredientDto::class.java)

            if (ingredientDto != null) {
                val documentRef = documentSnapshot.getDocumentReference("type")

                if (documentRef != null) {
                    ingredientDto.ingredientType = ingredientTypeRepository.getIngredientType(
                        documentRef.id
                    )
                }
            }

            return ingredientDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    private suspend fun getIngredientsFromQueryDocuments(documents: List<DocumentSnapshot>, getIngredientType: Boolean): List<IngredientDto> {
        val ingredients = ArrayList<IngredientDto>()

        for (document in documents) {
            val ingredientDto = document.toObject(IngredientDto::class.java)

            if (ingredientDto != null) {
                if (getIngredientType) {
                    val documentRef = document.getDocumentReference("type")

                    if (documentRef != null) {
                        ingredientDto.ingredientType = ingredientTypeRepository.getIngredientType(
                            documentRef.id
                        )
                    }
                }

                ingredientDto.id = document.id
                ingredients.add(ingredientDto)
            }
        }
        return ingredients
    }

    suspend fun getIngredients(): List<IngredientDto> {
        return try {
            val querySnapshot = ingredientsCollection.get().await()
            return getIngredientsFromQueryDocuments(querySnapshot.documents, getIngredientType = true)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getIngredientsForIngredientType(ingredientTypeId: String): List<IngredientDto> {
        return try {
            val querySnapshot = ingredientsCollection
                .whereEqualTo("type", ingredientTypesCollection.document(ingredientTypeId))
                .get()
                .await()
            return getIngredientsFromQueryDocuments(querySnapshot.documents, getIngredientType = false)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchIngredientsByTitle(lowercaseSearchWords: List<String>): List<IngredientDto> {
        return try {
            val querySnapshot = ingredientsCollection
                .whereArrayContainsAny("search_name", lowercaseSearchWords)
                .get()
                .await()

            return getIngredientsFromQueryDocuments(querySnapshot.documents, false)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}