package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.ProfileDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class ProfileRepository(db: FirebaseFirestore) {
    private val profileCollection = db.collection("profiles")
    private val dietaryInfoCollection = db.collection("dietary_info")

    private val dietaryInfoRepository = DietaryInfoRepository(db)

    suspend fun getProfileById(userId: String): ProfileDto? {
        return try {
            val querySnapshot = profileCollection
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return null
            }

            val documentSnapshot = querySnapshot.first()
            val profileDto = documentSnapshot.toObject(ProfileDto::class.java)

            profileDto.id = documentSnapshot.id
            profileDto.userId = documentSnapshot.get("user_id")?.toString()!!
            profileDto.picturePath = documentSnapshot.get("picture_path")?.toString()

            profileDto.dietaryInfo = dietaryInfoRepository.getDietaryInfoForReferences(documentSnapshot)

            return profileDto
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }

    suspend fun createOrUpdateProfile(profileDto: ProfileDto): Boolean {
        return try {
            profileDto.id = profileDto.id.ifEmpty {
                profileCollection.document().id
            }

            profileCollection.document(profileDto.id)
                .set(profileDto.toFirebaseMap(), SetOptions.merge())
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun ProfileDto.toFirebaseMap(): Map<String, Any?> {
        val dietaryInfoReferences = dietaryInfo.map { it.id }.map {
            dietaryInfoCollection.document(it)
        } ?: emptyList<DocumentReference>()

        return mapOf(
            "user_id" to userId,
            "username" to username,
            "dietary_info_references" to dietaryInfoReferences,
            "picture_path" to picturePath
        )
    }

}