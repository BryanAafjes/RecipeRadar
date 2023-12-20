package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.StepDto
import com.bth.reciperadar.domain.models.Step
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Test
import org.junit.Assert.assertEquals

class StepModelTest {
    @Test
    fun `Step toDto should map correctly`() {
        // Arrange
        val step = Step(
            title = "Mix Ingredients",
            description = "Mix everything together",
            number = 1,
            picturePath = "/images/mixing.jpg"
        )

        // Act
        val stepDto = step.toDto()

        // Assert
        assertEquals("Mix Ingredients", stepDto.title)
        assertEquals("Mix everything together", stepDto.description)
        assertEquals(1, stepDto.number)
        assertEquals("/images/mixing.jpg", stepDto.picturePath)
    }

    @Test
    fun `StepDto toDomain should map correctly`() {
        // Arrange
        val stepDto = StepDto(
            title = "Bake",
            description = "Bake in the oven",
            number = 2,
            picturePath = "/images/baking.jpg"
        )

        // Act
        val step = stepDto.toDomain()

        // Assert
        assertEquals("Bake", step.title)
        assertEquals("Bake in the oven", step.description)
        assertEquals(2, step.number)
        assertEquals("/images/baking.jpg", step.picturePath)
    }
}
