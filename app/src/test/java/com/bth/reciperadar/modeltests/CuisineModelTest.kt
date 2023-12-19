package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.CuisineDto
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CuisineModelTest {
    @Test
    fun `Cuisine toDto should return corresponding CuisineDto`() {
        // Arrange
        val cuisine = Cuisine(
            id = "1",
            name = "Italian",
            description = "Delicious Italian cuisine with pasta and pizza."
        )

        // Act
        val resultDto = cuisine.toDto()

        // Assert
        assertEquals("1", resultDto.id)
        assertEquals("Italian", resultDto.name)
        assertEquals("Delicious Italian cuisine with pasta and pizza.", resultDto.description)
    }

    @Test
    fun `CuisineDto toDomain should return corresponding Cuisine`() {
        // Arrange
        val cuisineDto = CuisineDto(
            id = "1",
            name = "Italian",
            description = "Delicious Italian cuisine with pasta and pizza."
        )

        // Act
        val resultCuisine = cuisineDto.toDomain()

        // Assert
        assertEquals("1", resultCuisine.id)
        assertEquals("Italian", resultCuisine.name)
        assertEquals("Delicious Italian cuisine with pasta and pizza.", resultCuisine.description)
    }
}
