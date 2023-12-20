package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.Profile
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.ProfileViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileViewModelTest {
    @Test
    fun `toViewModel should convert Profile to ProfileViewModel`() {
        // Arrange
        val profile = Profile(
            id = "1",
            userId = "user123",
            username = "John Doe",
            email = "john@example.com",
            picturePath = "path/to/picture",
            pictureDownloadUri = "http://example.com/picture",
            dietaryInfo = listOf(DietaryInfo(name = "Vegan"))
        )

        // Act
        val profileViewModel = profile.toViewModel()

        // Assert
        assertEquals(profile.id, profileViewModel.id)
        assertEquals(profile.userId, profileViewModel.userId)
        assertEquals(profile.username, profileViewModel.username)
        assertEquals(profile.email, profileViewModel.email)
        assertEquals(profile.picturePath, profileViewModel.picturePath)
        assertEquals(profile.pictureDownloadUri, profileViewModel.pictureDownloadUri)
        assertEquals(profile.dietaryInfo.size, profileViewModel.dietaryInfo.size)
    }

    @Test
    fun `toDomain should convert ProfileViewModel to Profile`() {
        // Arrange
        val profileViewModel = ProfileViewModel(
            id = "1",
            userId = "user123",
            username = "John Doe",
            email = "john@example.com",
            picturePath = "path/to/picture",
            pictureDownloadUri = "http://example.com/picture",
            dietaryInfo = listOf(DietaryInfoViewModel(id = "101", name = "Vegan", description = ""))
        )

        // Act
        val profile = profileViewModel.toDomain()

        // Assert
        assertEquals(profileViewModel.id, profile.id)
        assertEquals(profileViewModel.userId, profile.userId)
        assertEquals(profileViewModel.username, profile.username)
        assertEquals(profileViewModel.email, profile.email)
        assertEquals(profileViewModel.picturePath, profile.picturePath)
        assertEquals(profileViewModel.dietaryInfo.size, profile.dietaryInfo.size)
    }
}
