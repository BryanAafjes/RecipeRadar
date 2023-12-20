package com.bth.reciperadar.domaintests

import android.net.Uri
import androidx.core.net.toUri
import com.bth.reciperadar.data.dtos.ProfileDto
import com.bth.reciperadar.data.repositories.ProfileRepository
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.domain.models.Profile
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.spy
import org.robolectric.RobolectricTestRunner

class ProfileControllerUnitTests {
    private lateinit var authController: AuthController
    private lateinit var profileRepository: ProfileRepository
    private lateinit var profileController: ProfileController

    @Before
    fun arrange() {
        authController = mock(AuthController::class.java)
        profileRepository = mock(ProfileRepository::class.java)
        profileController = spy(ProfileController(authController, profileRepository))
    }

    @Test
    fun `getProfile should return null when getCurrentUserId is null`() = runTest {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn(null)

        // Act
        val result = profileController.getProfile()

        // Assert
        assertNull(result)
    }

    @Test
    fun `getProfile should return null when getProfileById returns null`() = runTest {
        // Arrange
        val userId = "user123"
        `when`(authController.getCurrentUserId()).thenReturn(userId)
        `when`(profileRepository.getProfileById(userId)).thenReturn(null)

        // Act
        val result = profileController.getProfile()

        // Assert
        assertNull(result)
    }

    @Test
    fun `getProfile should set email and pictureDownloadUri when profile is not null`() = runTest {
        // Arrange
        val userId = "user123"
        `when`(authController.getCurrentUserId()).thenReturn(userId)
        val mockProfileDto = ProfileDto(id = "1", userId = userId, username = "John Doe", email = "john@example.com", picturePath = "path/to/picture")
        `when`(profileRepository.getProfileById(userId)).thenReturn(mockProfileDto)
        `when`(authController.getCurrentUserEmail()).thenReturn("john@example.com")
        doReturn("http://example.com/picture").`when`(profileController).getImageDownloadUrlFromFirebase("path/to/picture")

        // Act
        val result = profileController.getProfile()

        // Assert
        assertNotNull(result)
        assertEquals("john@example.com", result?.email)
        assertEquals("http://example.com/picture", result?.pictureDownloadUri)
    }



    @Test
    fun `createOrUpdateProfile should return false when getCurrentUserId is null`() = runTest {
        // Arrange
        val mockProfile = Profile(id = "1", userId = "", username = "John Doe")
        val mockImageUri: Uri? = null // No image selected
        `when`(authController.getCurrentUserId()).thenReturn(null)

        // Act
        val result = profileController.createOrUpdateProfile(mockProfile, mockImageUri)

        // Assert
        assertFalse(result)
    }
}

@RunWith(RobolectricTestRunner::class)
class ProfileControllerUnitTestsRobolectric {
    private lateinit var authController: AuthController
    private lateinit var profileRepository: ProfileRepository
    private lateinit var profileController: ProfileController

    @Before
    fun arrange() {
        authController = mock(AuthController::class.java)
        profileRepository = mock(ProfileRepository::class.java)
        profileController = spy(ProfileController(authController, profileRepository))
    }

    @Test
    fun `createOrUpdateProfile should return false when uploadImageToFirebaseStorage returns null`() = runTest {
        // Arrange
        val mockProfile = Profile(id = "1", userId = "user123", username = "John Doe")
        val mockImageUri = "content://mock/image".toUri()
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        doReturn(null).`when`(profileController).getImageDownloadUrlFromFirebase("path/to/picture")

        // Act
        val result = profileController.createOrUpdateProfile(mockProfile, mockImageUri)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `createOrUpdateProfile should return true when uploadImageToFirebaseStorage is successful`() = runTest {
        // Arrange
        val mockProfile = Profile(id = "1", userId = "user123", username = "John Doe")
        val mockImageUri = Uri.parse("content://mock/image")
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        doReturn("path/to/image").`when`(profileController).uploadImageToFirebaseStorage(mockImageUri)
        `when`(profileRepository.createOrUpdateProfile(eq(mockProfile.toDto()))).thenReturn(true)
        doReturn(null).`when`(profileController).getImageDownloadUrlFromFirebase("path/to/picture")

        // Act
        val result = profileController.createOrUpdateProfile(mockProfile, mockImageUri)

        // Assert
        assertFalse(result)
    }
}


