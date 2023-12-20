package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.dtos.ProfileDto
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.Profile
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileModelTest {
    @Test
    fun `toDto should convert Profile to ProfileDto`() {
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
        val profileDto = profile.toDto()

        // Assert
        assertEquals(profile.id, profileDto.id)
        assertEquals(profile.userId, profileDto.userId)
        assertEquals(profile.username, profileDto.username)
        assertEquals(profile.email, profileDto.email)
        assertEquals(profile.picturePath, profileDto.picturePath)
        assertEquals(profile.dietaryInfo.size, profileDto.dietaryInfo.size)
    }

    @Test
    fun `toDomain should convert ProfileDto to Profile`() {
        // Arrange
        val profileDto = ProfileDto(
            id = "1",
            userId = "user123",
            username = "John Doe",
            email = "john@example.com",
            picturePath = "path/to/picture",
            dietaryInfo = listOf(DietaryInfoDto(name = "Vegan"))
        )

        // Act
        val profile = profileDto.toDomain()

        // Assert
        assertEquals(profileDto.id, profile.id)
        assertEquals(profileDto.userId, profile.userId)
        assertEquals(profileDto.username, profile.username)
        assertEquals(profileDto.email, profile.email)
        assertEquals(profileDto.picturePath, profile.picturePath)
        assertEquals(profileDto.dietaryInfo.size, profile.dietaryInfo.size)
    }
}
