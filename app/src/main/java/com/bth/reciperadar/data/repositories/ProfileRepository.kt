package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.ProfileDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(db: FirebaseFirestore) {
    private val profileCollection = db.collection("profiles")

    private val dietaryInfoRepository = DietaryInfoRepository(db)

    suspend fun getProfileById(userId: String): ProfileDto? {
        return try {
            val documentSnapshot = profileCollection
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .await()
                .first()

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

    suspend fun doesProfileExist(userId: String): Boolean {
        return try {
            val querySnapshot = profileCollection
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .await()

            return !querySnapshot.isEmpty
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            false
        }
    }
}