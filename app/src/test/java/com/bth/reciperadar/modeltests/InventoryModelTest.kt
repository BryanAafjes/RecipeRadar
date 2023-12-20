package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.InventoryDto
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.Inventory
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Assert.assertEquals
import org.junit.Test

class InventoryModelTest {
    @Test
    fun `toDto should map Inventory to InventoryDto`() {
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
        val result = inventory.toDto()

        // Assert
        val expected = InventoryDto(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot").toDto(),
                Ingredient(id = "102", name = "Tomato").toDto()
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toDomain should map InventoryDto to Inventory`() {
        // Arrange
        val inventoryDto = InventoryDto(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot").toDto(),
                Ingredient(id = "102", name = "Tomato").toDto()
            )
        )

        // Act
        val result = inventoryDto.toDomain()

        // Assert
        val expected = Inventory(
            id = "1",
            userId = "user123",
            ingredients = listOf(
                Ingredient(id = "101", name = "Carrot"),
                Ingredient(id = "102", name = "Tomato")
            )
        )
        assertEquals(expected, result)
    }
}