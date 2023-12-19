package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.CuisineDto
import com.bth.reciperadar.data.repositories.CuisineRepository
import com.bth.reciperadar.domain.controllers.CuisineController
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CuisineControllerUnitTests {
    private lateinit var cuisineRepository: CuisineRepository
    private lateinit var cuisineController: CuisineController

    @Before
    fun arrange() {
        // Arrange
        cuisineRepository = mock(CuisineRepository::class.java)
        cuisineController = CuisineController(cuisineRepository)
    }

    @Test
    fun `getCuisines should return list of cuisines`() = runTest {
        // Arrange
        val cuisineDtoList = listOf(
            CuisineDto(
                id = "1",
                name = "Italian",
                description = "Delicious Italian cuisine with pasta and pizza."
            ),
            CuisineDto(
                id = "2",
                name = "Japanese",
                description = "Authentic Japanese dishes like sushi and ramen."
            )
        )
        `when`(cuisineRepository.getCuisines()).thenReturn(cuisineDtoList)

        // Act
        val result = cuisineController.getCuisines()

        // Assert
        assertEquals(cuisineDtoList.map { it.toDomain() }, result)
    }

    @Test
    fun `getCuisines should return empty list on repository error`() = runTest {
        // Arrange
        `when`(cuisineRepository.getCuisines()).thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = cuisineController.getCuisines()

        // Assert
        assertEquals(emptyList<Cuisine>(), result)
    }
}