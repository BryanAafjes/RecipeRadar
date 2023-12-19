package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class DietaryInfoModelTest {
    @Test
    fun `DietaryInfo toDto should return corresponding DietaryInfoDto`() {
        // Arrange
        val dietaryInfo = DietaryInfo(
            id = "1",
            name = "Gluten-Free",
            description = "Foods that do not contain gluten."
        )

        // Act
        val resultDto = dietaryInfo.toDto()

        // Assert
        assertEquals("1", resultDto.id)
        assertEquals("Gluten-Free", resultDto.name)
        assertEquals("Foods that do not contain gluten.", resultDto.description)
    }

    @Test
    fun `DietaryInfoDto toDomain should return corresponding DietaryInfo`() {
        // Arrange
        val dietaryInfoDto = DietaryInfoDto(
            id = "1",
            name = "Gluten-Free",
            description = "Foods that do not contain gluten."
        )

        // Act
        val resultDietaryInfo = dietaryInfoDto.toDomain()

        // Assert
        assertEquals("1", resultDietaryInfo.id)
        assertEquals("Gluten-Free", resultDietaryInfo.name)
        assertEquals("Foods that do not contain gluten.", resultDietaryInfo.description)
    }
}
