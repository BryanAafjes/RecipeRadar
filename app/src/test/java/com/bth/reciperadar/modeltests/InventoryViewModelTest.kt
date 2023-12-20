package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.domain.models.Inventory
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.InventoryViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class InventoryViewModelTest {
    @Test
    fun `toViewModel should map Inventory to InventoryViewModel`() {
        // Arrange
        val inventory = Inventory(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot"),
                Ingredient(id = "102", name = "Tomato")
            )
        )

        // Act
        val result = inventory.toViewModel()

        // Assert
        val expected = InventoryViewModel(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot").toViewModel(),
                Ingredient(id = "102", name = "Tomato").toViewModel()
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toDomain should map InventoryViewModel to Inventory`() {
        // Arrange
        val ingredientViewModel1 = IngredientViewModel(
            id = "101",
            name = "Carrot",
            description = "Orange vegetable",
            amount = "1",
            ingredientType = IngredientTypeViewModel(id = "1", name = "Vegetable", emptyList())
        )

        val ingredientViewModel2 = IngredientViewModel(
            id = "102",
            name = "Tomato",
            description = "Red fruit",
            amount = "2",
            ingredientType = IngredientTypeViewModel(id = "2", name = "Fruit", emptyList())
        )

        val inventoryViewModel = InventoryViewModel(
            id = "1",
            userId = "user123",
            ingredients = listOf(ingredientViewModel1, ingredientViewModel2)
        )

        // Act
        val result = inventoryViewModel.toDomain()

        // Assert
        val expected = Inventory(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(
                    id = "101",
                    name = "Carrot",
                    description = "Orange vegetable",
                    amount = "1",
                    ingredientType = IngredientType(id = "1", name = "Vegetable", emptyList())
                ),
                Ingredient(
                    id = "102",
                    name = "Tomato",
                    description = "Red fruit",
                    amount = "2",
                    ingredientType = IngredientType(id = "2", name = "Fruit", emptyList())
                )
            )
        )
        assertEquals(expected, result)
    }

}
