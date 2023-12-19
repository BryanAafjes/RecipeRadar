package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.presentation.viewmodels.CuisineViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CuisineViewModelTest {
    @Test
    fun `CuisineViewModel toDomain should return corresponding Cuisine`() {
        // Arrange
        val cuisineViewModel = CuisineViewModel(
            id = "1",
            name = "Italian",
            description = "Delicious Italian cuisine with pasta and pizza."
        )

        // Act
        val resultCuisine = cuisineViewModel.toDomain()

        // Assert
        assertEquals("1", resultCuisine.id)
        assertEquals("Italian", resultCuisine.name)
        assertEquals("Delicious Italian cuisine with pasta and pizza.", resultCuisine.description)
    }

    @Test
    fun `Cuisine toViewModel should return corresponding CuisineViewModel`() {
        // Arrange
        val cuisine = Cuisine(
            id = "1",
            name = "Italian",
            description = "Delicious Italian cuisine with pasta and pizza."
        )

        // Act
        val resultViewModel = cuisine.toViewModel()

        // Assert
        assertEquals("1", resultViewModel.id)
        assertEquals("Italian", resultViewModel.name)
        assertEquals("Delicious Italian cuisine with pasta and pizza.", resultViewModel.description)
    }
}
