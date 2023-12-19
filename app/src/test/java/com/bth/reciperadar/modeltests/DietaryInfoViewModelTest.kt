package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class DietaryInfoViewModelTest {
    @Test
    fun `DietaryInfoViewModel toDomain should return corresponding DietaryInfo`() {
        // Arrange
        val dietaryInfoViewModel = DietaryInfoViewModel(
            id = "1",
            name = "Gluten-Free",
            description = "Foods that do not contain gluten."
        )

        // Act
        val resultDietaryInfo = dietaryInfoViewModel.toDomain()

        // Assert
        assertEquals("1", resultDietaryInfo.id)
        assertEquals("Gluten-Free", resultDietaryInfo.name)
        assertEquals("Foods that do not contain gluten.", resultDietaryInfo.description)
    }

    @Test
    fun `DietaryInfo toViewModel should return corresponding DietaryInfoViewModel`() {
        // Arrange
        val dietaryInfo = DietaryInfo(
            id = "1",
            name = "Gluten-Free",
            description = "Foods that do not contain gluten."
        )

        // Act
        val resultViewModel = dietaryInfo.toViewModel()

        // Assert
        assertEquals("1", resultViewModel.id)
        assertEquals("Gluten-Free", resultViewModel.name)
        assertEquals("Foods that do not contain gluten.", resultViewModel.description)
    }
}
