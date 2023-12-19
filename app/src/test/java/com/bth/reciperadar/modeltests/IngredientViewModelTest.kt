package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientViewModelTest {
    @Test
    fun `toViewModel should convert Ingredient to IngredientViewModel`() {
        // Arrange
        val ingredient = Ingredient(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientType(id = "1", name = "Dry"),
            amount = "1 cup"
        )

        // Act
        val resultViewModel = ingredient.toViewModel()

        // Assert
        assertEquals("1", resultViewModel.id)
        assertEquals("Flour", resultViewModel.name)
        assertEquals("White flour", resultViewModel.description)
        assertEquals("1", resultViewModel.ingredientType?.id)
        assertEquals("Dry", resultViewModel.ingredientType?.name)
        assertEquals("1 cup", resultViewModel.amount)
    }

    @Test
    fun `toDomain should convert IngredientViewModel to Ingredient`() {
        // Arrange
        val ingredientViewModel = IngredientViewModel(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientTypeViewModel(id = "1", name = "Dry", ingredients = emptyList()),
            amount = "1 cup"
        )

        // Act
        val resultIngredient = ingredientViewModel.toDomain()

        // Assert
        assertEquals("1", resultIngredient.id)
        assertEquals("Flour", resultIngredient.name)
        assertEquals("White flour", resultIngredient.description)
        assertEquals("1", resultIngredient.ingredientType?.id)
        assertEquals("Dry", resultIngredient.ingredientType?.name)
        assertEquals(emptyList<Ingredient>(), resultIngredient.ingredientType?.ingredients)
        assertEquals("1 cup", resultIngredient.amount)
    }
}
