package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.dtos.IngredientDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DietaryInfoRepository(db: FirebaseFirestore) {
    private val dietaryInfoCollection = db.collection("dietary_info")

    suspend fun getDietaryInfoForRecipe(document: DocumentSnapshot): List<DietaryInfoDto> {
        val dietaryInfoList = ArrayList<DietaryInfoDto>()
        val firestoreDietaryInfoReferences: List<DocumentReference> = document.get("dietary_info_references") as List<DocumentReference>
        firestoreDietaryInfoReferences.forEach { reference ->
            val dietaryInfoId = reference.id
            val dietaryInfoDto: DietaryInfoDto? = getDietaryInfo(dietaryInfoId)

            if(dietaryInfoDto != null) {
                dietaryInfoDto.id = dietaryInfoId
                dietaryInfoList.add(dietaryInfoDto)
            }
        }

        return dietaryInfoList
    }
    suspend fun getDietaryInfo(dietaryInfoIdDto: String): DietaryInfoDto? {
        return try {
            val documentSnapshot = dietaryInfoCollection.document(dietaryInfoIdDto).get().await()
            val dietaryInfoDto = documentSnapshot.toObject(DietaryInfoDto::class.java)

            return dietaryInfoDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }
}