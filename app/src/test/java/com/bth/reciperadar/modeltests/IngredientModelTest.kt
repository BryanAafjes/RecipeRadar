package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.IngredientTypeDto
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientModelTest {
    @Test
    fun `toDto should convert Ingredient to IngredientDto`() {
        // Arrange
        val ingredient = Ingredient(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientType(id = "1", name = "Dry"),
            amount = "1 cup"
        )

        // Act
        val resultDto = ingredient.toDto()

        // Assert
        assertEquals("1", resultDto.id)
        assertEquals("Flour", resultDto.name)
        assertEquals("White flour", resultDto.description)
        assertEquals("1", resultDto.ingredientType?.id)
        assertEquals("Dry", resultDto.ingredientType?.name)
        assertEquals("1 cup", resultDto.amount)
    }

    @Test
    fun `toDomain should convert IngredientDto to Ingredient`() {
        // Arrange
        val ingredientDto = IngredientDto(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientTypeDto(id = "1", name = "Dry"),
            amount = "1 cup"
        )

        // Act
        val resultIngredient = ingredientDto.toDomain()

        // Assert
        assertEquals("1", resultIngredient.id)
        assertEquals("Flour", resultIngredient.name)
        assertEquals("White flour", resultIngredient.description)
        assertEquals("1", resultIngredient.ingredientType?.id)
        assertEquals("Dry", resultIngredient.ingredientType?.name)
        assertEquals("1 cup", resultIngredient.amount)
    }
}
