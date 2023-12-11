package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.CuisineDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CuisineRepository(db: FirebaseFirestore) {
    private val cuisineCollection = db.collection("cuisines")

    suspend fun getDietaryInfoForRecipe(document: DocumentSnapshot): List<CuisineDto> {
        val cuisineList = ArrayList<CuisineDto>()
        val firestoreCuisineReferences: List<DocumentReference> = document.get("cuisine_references") as List<DocumentReference>
        firestoreCuisineReferences.forEach { reference ->
            val cuisineId = reference.id
            val cuisineDto: CuisineDto? = getCuisine(cuisineId)

            if(cuisineDto != null) {
                cuisineDto.id = cuisineId
                cuisineList.add(cuisineDto)
            }
        }

        return cuisineList
    }

    suspend fun getCuisine(cuisineId: String): CuisineDto? {
        return try {
            val documentSnapshot = cuisineCollection.document(cuisineId).get().await()
            val cuisineDto = documentSnapshot.toObject(CuisineDto::class.java)

            return cuisineDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    suspend fun getCuisines(): List<CuisineDto> {
        return try {
            val querySnapshot = cuisineCollection.get().await()
            val cuisineList = ArrayList<CuisineDto>()

            for(document in querySnapshot.documents) {
                val cuisine = document.toObject(CuisineDto::class.java)

                if(cuisine != null) {
                    cuisine.id = document.id

                    cuisineList.add(cuisine)
                }
            }

            return cuisineList
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}