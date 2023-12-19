package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.IngredientTypeDto
import com.bth.reciperadar.data.repositories.IngredientRepository
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class IngredientControllerUnitTests {

    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var ingredientController: IngredientController

    @Before
    fun arrange() {
        ingredientRepository = mock(IngredientRepository::class.java)
        ingredientController = IngredientController(ingredientRepository)
    }

    @Test
    fun `getIngredients should return list of ingredients`() = runTest {
        // Arrange
        val ingredientDto1 = IngredientDto(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientTypeDto(id = "1", name = "Baking ingredients"),
            amount = "1 cup"
        )

        val ingredientDto2 = IngredientDto(
            id = "2",
            name = "Sugar",
            description = "Granulated sugar",
            ingredientType = IngredientTypeDto(id = "2", name = "Baking ingredients"),
            amount = "1/2 cup"
        )

        val ingredientDtoList = listOf(ingredientDto1, ingredientDto2)
        `when`(ingredientRepository.getIngredients()).thenReturn(ingredientDtoList)

        // Act
        val result = ingredientController.getIngredients()

        // Assert
        assertEquals(ingredientDtoList.map { it.toDomain() }, result)
    }

    @Test
    fun `getIngredientsForIngredientType should return list of ingredients for a specific type`() = runTest {
        // Arrange
        val ingredientTypeId = "1"
        val ingredientDto1 = IngredientDto(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientTypeDto(id = ingredientTypeId, name = "Baking ingredients"),
            amount = "1 cup"
        )

        val ingredientDto2 = IngredientDto(
            id = "2",
            name = "Sugar",
            description = "Granulated sugar",
            ingredientType = IngredientTypeDto(id = "2", name = "Baking ingredients"),
            amount = "1/2 cup"
        )

        val ingredientDtoList = listOf(ingredientDto1, ingredientDto2)
        `when`(ingredientRepository.getIngredientsForIngredientType(ingredientTypeId)).thenReturn(ingredientDtoList)

        // Act
        val result = ingredientController.getIngredientsForIngredientType(ingredientTypeId)

        // Assert
        assertEquals(ingredientDtoList.map { it.toDomain() }, result)
    }

    @Test
    fun `searchIngredientsByName should return ingredient based on search query`() = runTest {
        // Arrange
        val searchQuery = "Flour"

        val searchQueryLowerCase = listOf("flour")

        val ingredientDto = IngredientDto(
            id = "1",
            name = "Flour",
            description = "White flour",
            ingredientType = IngredientTypeDto(id = "1", name = "Baking ingredients"),
            amount = "1 cup"
        )

        `when`(ingredientRepository.searchIngredientsByTitle(searchQueryLowerCase)).thenReturn(listOf(ingredientDto))

        // Act
        val result = ingredientController.searchIngredientsByName(searchQuery)

        // Assert
        assertEquals(ingredientDto.toDomain(), result)
    }

    @Test
    fun `getIngredients should return empty list on repository error`() = runTest {
        // Arrange
        `when`(ingredientRepository.getIngredients()).thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = ingredientController.getIngredients()

        // Assert
        assertEquals(emptyList<Ingredient>(), result)
    }

    @Test
    fun `getIngredientsForIngredientType should return empty list on repository error`() = runTest {
        // Arrange
        val ingredientTypeId = "1"
        `when`(ingredientRepository.getIngredientsForIngredientType(ingredientTypeId))
            .thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = ingredientController.getIngredientsForIngredientType(ingredientTypeId)

        // Assert
        assertEquals(emptyList<Ingredient>(), result)
    }

    @Test
    fun `searchIngredientsByName should return null on repository error`() = runTest {
        // Arrange
        val searchQuery = "Flour"
        val searchQueryLowerCase = listOf("flour")

        `when`(ingredientRepository.searchIngredientsByTitle(searchQueryLowerCase))
            .thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = ingredientController.searchIngredientsByName(searchQuery)

        // Assert
        assertEquals(null, result)
    }
}