package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.IngredientTypeDto
import com.bth.reciperadar.data.repositories.IngredientTypeRepository
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.models.IngredientType
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.RuntimeException

class IngredientTypeControllerUnitTests {
    private lateinit var ingredientTypeRepository: IngredientTypeRepository
    private lateinit var ingredientTypeController: IngredientTypeController

    @Before
    fun arrange() {
        ingredientTypeRepository = mock(IngredientTypeRepository::class.java)
        ingredientTypeController = IngredientTypeController(ingredientTypeRepository)
    }

    @Test
    fun `getIngredientTypes should return list of ingredient types`() = runTest {
        // Arrange
        val ingredientTypeDto1 = IngredientTypeDto(id = "1", name = "Vegetables")
        val ingredientTypeDto2 = IngredientTypeDto(id = "2", name = "Meat")

        val ingredientTypeDtoList = listOf(ingredientTypeDto1, ingredientTypeDto2)
        `when`(ingredientTypeRepository.getIngredientTypes()).thenReturn(ingredientTypeDtoList)

        // Act
        val result = ingredientTypeController.getIngredientTypes()

        // Assert
        assertEquals(ingredientTypeDtoList.map { it.toDomain() }, result)
    }

    @Test
    fun `getIngredientTypes should return empty list on repository error`() = runTest {
        // Arrange
        `when`(ingredientTypeRepository.getIngredientTypes()).thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = ingredientTypeController.getIngredientTypes()

        // Assert
        assertEquals(emptyList<IngredientType>(), result)
    }
}
