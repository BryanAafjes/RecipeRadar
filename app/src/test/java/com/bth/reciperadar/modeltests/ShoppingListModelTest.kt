package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.ShoppingListDto
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.ShoppingList
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Test
import org.junit.Assert.assertEquals

class ShoppingListModelTest {
    @Test
    fun `ShoppingList toDto should map correctly`() {
        // Arrange
        val shoppingList = ShoppingList(
            id = "1",
            userId = "user123",
            ingredients = listOf(Ingredient(id = "2", name = "Tomato")),
            checkedIngredients = listOf(Ingredient(id = "3", name = "Pasta"))
        )

        // Act
        val shoppingListDto = shoppingList.toDto()

        // Assert
        assertEquals("1", shoppingListDto.id)
        assertEquals("user123", shoppingListDto.userId)
        assertEquals(1, shoppingListDto.ingredients.size)
        assertEquals("Tomato", shoppingListDto.ingredients[0].name)
        assertEquals(1, shoppingListDto.checkedIngredients.size)
        assertEquals("Pasta", shoppingListDto.checkedIngredients[0].name)
    }

    @Test
    fun `ShoppingListDto toDomain should map correctly`() {
        // Arrange
        val shoppingListDto = ShoppingListDto(
            id = "1",
            userId = "user123",
            ingredients = listOf(IngredientDto(id = "2", name = "Tomato")),
            checkedIngredients = listOf(IngredientDto(id = "3", name = "Pasta"))
        )

        // Act
        val shoppingList = shoppingListDto.toDomain()

        // Assert
        assertEquals("1", shoppingList.id)
        assertEquals("user123", shoppingList.userId)
        assertEquals(1, shoppingList.ingredients.size)
        assertEquals("Tomato", shoppingList.ingredients[0].name)
        assertEquals(1, shoppingList.checkedIngredients.size)
        assertEquals("Pasta", shoppingList.checkedIngredients[0].name)
    }
}
