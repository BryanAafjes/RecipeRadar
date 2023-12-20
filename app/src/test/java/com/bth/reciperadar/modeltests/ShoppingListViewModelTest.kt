package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.ShoppingList
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.ShoppingListViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Test
import org.junit.Assert.assertEquals

class ShoppingListViewModelTest {

    @Test
    fun `ShoppingList toViewModel should map correctly`() {
        // Arrange
        val shoppingList = ShoppingList(
            id = "1",
            userId = "user123",
            ingredients = listOf(Ingredient(id = "2", name = "Tomato")),
            checkedIngredients = listOf(Ingredient(id = "3", name = "Pasta"))
        )

        // Act
        val shoppingListViewModel = shoppingList.toViewModel()

        // Assert
        assertEquals("1", shoppingListViewModel.id)
        assertEquals("user123", shoppingListViewModel.userId)
        assertEquals(1, shoppingListViewModel.ingredients.size)
        assertEquals("Tomato", shoppingListViewModel.ingredients[0].name)
        assertEquals(1, shoppingListViewModel.checkedIngredients.size)
        assertEquals("Pasta", shoppingListViewModel.checkedIngredients[0].name)
    }

    @Test
    fun `ShoppingListViewModel toDomain should map correctly`() {
        // Arrange
        val shoppingListViewModel = ShoppingListViewModel(
            id = "1",
            userId = "user123",
            ingredients = listOf(IngredientViewModel(id = "2", name = "Tomato", amount = "", description = "", ingredientType = null)),
            checkedIngredients = listOf(IngredientViewModel(id = "3", name = "Pasta", amount = "", description = "", ingredientType = null))
        )

        // Act
        val shoppingList = shoppingListViewModel.toDomain()

        // Assert
        assertEquals("1", shoppingList.id)
        assertEquals("user123", shoppingList.userId)
        assertEquals(1, shoppingList.ingredients.size)
        assertEquals("Tomato", shoppingList.ingredients[0].name)
        assertEquals(1, shoppingList.checkedIngredients.size)
        assertEquals("Pasta", shoppingList.checkedIngredients[0].name)
    }
}
