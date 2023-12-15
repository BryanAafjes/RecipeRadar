package com.bth.reciperadar.domain.controllers

import com.bth.reciperadar.data.repositories.ProfileRepository
import com.bth.reciperadar.domain.models.Profile
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileController(
    private val authController: AuthController,
    private val profileRepository: ProfileRepository
) {

    suspend fun getProfile(): Profile? {
        return withContext(Dispatchers.IO) {
            val userId = authController.auth.currentUser?.uid

            if (userId != null) {
                try {
                    val profile = profileRepository.getProfileById(userId)?.toDomain()

                    if (profile != null) {
                        profile.email = authController.auth.currentUser?.email
                    }

                    return@withContext profile
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            null
        }
    }

    suspend fun createOrUpdateProfile(profile: Profile): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                profile.userId = authController.auth.currentUser?.uid ?: ""
                profileRepository.createOrUpdateProfile(profile.toDto())
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}