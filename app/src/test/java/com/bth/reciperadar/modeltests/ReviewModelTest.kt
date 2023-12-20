package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.ReviewDto
import com.bth.reciperadar.domain.enums.ReviewRatingEnum
import com.bth.reciperadar.domain.models.Review
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Test
import org.junit.Assert.assertEquals

class ReviewModelTest {
    @Test
    fun `Review toDto should map correctly`() {
        // Arrange
        val review = Review(
            id = "1",
            userId = "user123",
            rating = ReviewRatingEnum.FourStars
        )

        // Act
        val reviewDto = review.toDto()

        // Assert
        assertEquals("1", reviewDto.id)
        assertEquals("user123", reviewDto.userId)
        assertEquals(4, reviewDto.rating)
    }

    @Test
    fun `ReviewDto toDomain should map correctly`() {
        // Arrange
        val reviewDto = ReviewDto(
            id = "1",
            userId = "user123",
            rating = 3
        )

        // Act
        val review = reviewDto.toDomain()

        // Assert
        assertEquals("1", review.id)
        assertEquals("user123", review.userId)
        assertEquals(ReviewRatingEnum.ThreeStars, review.rating)
    }

    @Test
    fun `ReviewDto toDomain should default to FiveStars for unknown rating`() {
        // Arrange
        val reviewDto = ReviewDto(
            id = "1",
            userId = "user123",
            rating = 6 // Unknown rating
        )

        // Act
        val review = reviewDto.toDomain()

        // Assert
        assertEquals("1", review.id)
        assertEquals("user123", review.userId)
        assertEquals(ReviewRatingEnum.FiveStars, review.rating)
    }
}