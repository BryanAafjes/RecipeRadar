package com.bth.reciperadar.domain.controllers

import android.net.Uri
import com.bth.reciperadar.data.repositories.ProfileRepository
import com.bth.reciperadar.domain.models.Profile
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileController(
    private val authController: AuthController,
    private val profileRepository: ProfileRepository
) {

    suspend fun getProfile(): Profile? {
        return withContext(Dispatchers.IO) {
            val userId = authController.getCurrentUserId()

            if (userId != null) {
                try {
                    val profile = profileRepository.getProfileById(userId)?.toDomain()

                    if (profile != null) {
                        profile.email = authController.getCurrentUserEmail()

                        if(profile.picturePath != null) {
                            try {
                                profile.pictureDownloadUri = getImageDownloadUrlFromFirebase(profile.picturePath!!)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                profile.picturePath = null
                            }
                        }
                    }

                    return@withContext profile
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            null
        }
    }

    suspend fun createOrUpdateProfile(profile: Profile, selectedImageUri: Uri?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (selectedImageUri != null) {
                    val resultRef = uploadImageToFirebaseStorage(selectedImageUri)

                    if (resultRef != null){
                        profile.picturePath = resultRef
                    }
                }

                profile.userId = authController.getCurrentUserId() ?: ""
                profileRepository.createOrUpdateProfile(profile.toDto())
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun uploadImageToFirebaseStorage(fileUri: Uri): String? {
        val userId = authController.getCurrentUserId()

        if (userId != null) {
            val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("profile_pictures")
            val fileRef: StorageReference = storageRef.child(userId)

            val uploadTask = fileRef.putFile(fileUri)
            try {
                uploadTask.await()

                return fileRef.path
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    suspend fun getImageDownloadUrlFromFirebase(referenceString: String): String? {
        val downloadURL = FirebaseStorage
            .getInstance()
            .getReference(referenceString)
            .downloadUrl
            .await()

        if (downloadURL != null) {
            return downloadURL.toString()
        }

        return null
    }
}