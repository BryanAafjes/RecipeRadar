package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Step
import com.bth.reciperadar.presentation.viewmodels.StepViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Test
import org.junit.Assert.assertEquals

class StepViewModelTest {
    @Test
    fun `Step toViewModel should map correctly`() {
        // Arrange
        val step = Step(
            title = "Chop Vegetables",
            description = "Chop all vegetables finely",
            number = 1,
            picturePath = "/images/chopping.jpg"
        )

        // Act
        val stepViewModel = step.toViewModel()

        // Assert
        assertEquals("Chop Vegetables", stepViewModel.title)
        assertEquals("Chop all vegetables finely", stepViewModel.description)
        assertEquals(1, stepViewModel.number)
        assertEquals("/images/chopping.jpg", stepViewModel.picturePath)
    }

    @Test
    fun `StepViewModel toDomain should map correctly`() {
        // Arrange
        val stepViewModel = StepViewModel(
            title = "Saute",
            description = "Saute in a pan",
            number = 2,
            picturePath = "/images/sauteing.jpg"
        )

        // Act
        val step = stepViewModel.toDomain()

        // Assert
        assertEquals("Saute", step.title)
        assertEquals("Saute in a pan", step.description)
        assertEquals(2, step.number)
        assertEquals("/images/sauteing.jpg", step.picturePath)
    }
}
