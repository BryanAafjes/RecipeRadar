package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.enums.ReviewRatingEnum
import com.bth.reciperadar.domain.models.Review
import com.bth.reciperadar.presentation.viewmodels.ReviewViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Test
import org.junit.Assert.assertEquals

class ReviewViewModelTest {
    @Test
    fun `Review toViewModel should map correctly`() {
        // Arrange
        val review = Review(
            id = "1",
            userId = "user123",
            rating = ReviewRatingEnum.FourStars
        )

        // Act
        val reviewViewModel = review.toViewModel()

        // Assert
        assertEquals("1", reviewViewModel.id)
        assertEquals("user123", reviewViewModel.userId)
        assertEquals(ReviewRatingEnum.FourStars, reviewViewModel.rating)
    }

    @Test
    fun `ReviewViewModel toDomain should map correctly`() {
        // Arrange
        val reviewViewModel = ReviewViewModel(
            id = "1",
            userId = "user123",
            rating = ReviewRatingEnum.ThreeStars
        )

        // Act
        val review = reviewViewModel.toDomain()

        // Assert
        assertEquals("1", review.id)
        assertEquals("user123", review.userId)
        assertEquals(ReviewRatingEnum.ThreeStars, review.rating)
    }
}
