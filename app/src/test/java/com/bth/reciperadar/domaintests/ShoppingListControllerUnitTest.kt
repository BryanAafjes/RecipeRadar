package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.ShoppingListDto
import com.bth.reciperadar.data.repositories.ShoppingListRepository
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.ShoppingListController
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.ShoppingList
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class ShoppingListControllerUnitTest {

    private lateinit var authController: AuthController
    private lateinit var shoppingListRepository: ShoppingListRepository
    private lateinit var shoppingListController: ShoppingListController

    @Before
    fun setUp() {
        authController = mock(AuthController::class.java)
        shoppingListRepository = mock(ShoppingListRepository::class.java)
        shoppingListController = ShoppingListController(authController, shoppingListRepository)
    }

    @Test
    fun `getShoppingList should return shopping list when user is authenticated`() = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        `when`(shoppingListRepository.getShoppingListByUserId("user123")).thenReturn(ShoppingListDto(id = "1", userId = "user123"))

        // Act
        val result = shoppingListController.getShoppingList()

        // Assert
        assertEquals("1", result?.id)
    }

    @Test
    fun `getShoppingList should return null when user is not authenticated`() = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn(null)

        // Act
        val result = shoppingListController.getShoppingList()

        // Assert
        assertEquals(null, result)
    }

    @Test
    fun `createOrUpdateShoppingList should return true when user is authenticated`(): Unit = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        val shoppingList = ShoppingList(id = "1", userId = "user123")
        `when`(shoppingListRepository.createOrUpdateShoppingList(shoppingList.toDto())).thenReturn(true)

        // Act
        val result = shoppingListController.createOrUpdateShoppingList(shoppingList)

        // Assert
        assertEquals(true, result)
        verify(shoppingListRepository, times(1)).createOrUpdateShoppingList(shoppingList.toDto())
    }

    @Test
    fun `createOrUpdateShoppingList should return false when user is not authenticated`(): Unit = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn(null)
        val shoppingList = ShoppingList(id = "1", userId = "user123")

        // Act
        val result = shoppingListController.createOrUpdateShoppingList(shoppingList)

        // Assert
        assertEquals(false, result)
        verify(shoppingListRepository, never()).createOrUpdateShoppingList(ShoppingListDto(id = "1", userId = "user123"))
    }

    @Test
    fun `addIngredientListToShoppingList should return true when shopping list exists`(): Unit = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        `when`(shoppingListRepository.getShoppingListByUserId("user123")).thenReturn(ShoppingListDto(id = "1", userId = "user123"))
        val ingredients = listOf(Ingredient(id = "1", name = "Tomato"))

        // Act
        val result = shoppingListController.addIngredientListToShoppingList(ingredients)

        // Assert
        assertEquals(true, result)
        verify(shoppingListRepository, times(1)).createOrUpdateShoppingList(ShoppingListDto(id = "1", userId = "user123", ingredients = ingredients.map { it.toDto() }))
    }

    @Test
    fun `addIngredientListToShoppingList should return false when shopping list is null`(): Unit = runBlocking {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        `when`(shoppingListRepository.getShoppingListByUserId("user123")).thenReturn(null)
        val ingredients = listOf(Ingredient(id = "1", name = "Tomato"))

        // Act
        val result = shoppingListController.addIngredientListToShoppingList(ingredients)

        // Assert
        assertEquals(false, result)
        verify(shoppingListRepository, never()).createOrUpdateShoppingList(ShoppingListDto(id = "1", userId = "user123"))
    }

    @Test
    fun `addIngredientListToShoppingList should return false when an exception occurs`(): Unit = runBlocking {
        // Arrange
        val ingredients = listOf(Ingredient(id = "1", name = "Tomato"))
        `when`(authController.getCurrentUserId()).thenReturn("user123")
        `when`(shoppingListRepository.getShoppingListByUserId("user123")).thenThrow(RuntimeException("Simulated exception"))

        // Act
        val result = shoppingListController.addIngredientListToShoppingList(ingredients)

        // Assert
        assertEquals(false, result)
    }
}
