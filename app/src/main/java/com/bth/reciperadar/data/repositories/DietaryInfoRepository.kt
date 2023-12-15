package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DietaryInfoRepository(db: FirebaseFirestore) {
    private val dietaryInfoCollection = db.collection("dietary_info")

    suspend fun getDietaryInfoForReferences(document: DocumentSnapshot): List<DietaryInfoDto> {
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

    suspend fun getDietaryInfo(dietaryInfoDto: String): DietaryInfoDto? {
        return try {
            val documentSnapshot = dietaryInfoCollection.document(dietaryInfoDto).get().await()
            return documentSnapshot.toObject(DietaryInfoDto::class.java)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    suspend fun getDietaryInfoList(): List<DietaryInfoDto> {
        return try {
            val querySnapshot = dietaryInfoCollection.get().await()
            val dietaryInfoList = ArrayList<DietaryInfoDto>()

            for(document in querySnapshot.documents) {
                val dietaryInfoDto = document.toObject(DietaryInfoDto::class.java)

                if(dietaryInfoDto != null) {
                    dietaryInfoDto.id = document.id

                    dietaryInfoList.add(dietaryInfoDto)
                }
            }

            return dietaryInfoList
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}