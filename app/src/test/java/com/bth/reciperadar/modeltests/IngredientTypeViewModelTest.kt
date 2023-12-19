package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientTypeViewModelTest {
    @Test
    fun `toViewModel should convert IngredientType to IngredientTypeViewModel`() {
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
        val resultViewModel = ingredientType.toViewModel()

        // Assert
        assertEquals("1", resultViewModel.id)
        assertEquals("Vegetables", resultViewModel.name)
        assertEquals(2, resultViewModel.ingredients?.size)
        assertEquals("101", resultViewModel.ingredients?.get(0)?.id)
        assertEquals("Carrot", resultViewModel.ingredients?.get(0)?.name)
        assertEquals("102", resultViewModel.ingredients?.get(1)?.id)
        assertEquals("Broccoli", resultViewModel.ingredients?.get(1)?.name)
    }

    @Test
    fun `toDomain should convert IngredientTypeViewModel to IngredientType`() {
        // Arrange
        val ingredientTypeViewModel = IngredientTypeViewModel(
            id = "1",
            name = "Vegetables",
            ingredients = emptyList()
        )

        // Act
        val resultIngredientType = ingredientTypeViewModel.toDomain()

        // Assert
        assertEquals("1", resultIngredientType.id)
        assertEquals("Vegetables", resultIngredientType.name)
        assertEquals(0, resultIngredientType.ingredients?.size)
        assertEquals(emptyList<Ingredient>(), resultIngredientType.ingredients)
    }
}
