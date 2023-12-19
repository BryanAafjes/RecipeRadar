package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.IngredientTypeDto
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientTypeModelTest {
    @Test
    fun `toDto should convert IngredientType to IngredientTypeDto`() {
        // Arrange
        val ingredientType = IngredientType(
            id = "1",
            name = "Vegetables",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot"),
                Ingredient(id = "102", name = "Broccoli")
            )
        )

        // Act
        val resultDto = ingredientType.toDto()

        // Assert
        assertEquals("1", resultDto.id)
        assertEquals("Vegetables", resultDto.name)
    }

    @Test
    fun `toDomain should convert IngredientTypeDto to IngredientType`() {
        // Arrange
        val ingredientTypeDto = IngredientTypeDto(
            id = "1",
            name = "Vegetables"
        )

        // Act
        val resultIngredientType = ingredientTypeDto.toDomain()

        // Assert
        assertEquals("1", resultIngredientType.id)
        assertEquals("Vegetables", resultIngredientType.name)
    }
}
