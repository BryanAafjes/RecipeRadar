package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.repositories.DietaryInfoRepository
import com.bth.reciperadar.domain.controllers.DietaryInfoController
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.toDomain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DietaryInfoControllerUnitTests {

    private lateinit var dietaryInfoRepository: DietaryInfoRepository
    private lateinit var dietaryInfoController: DietaryInfoController

    @Before
    fun arrange() {
        dietaryInfoRepository = mock(DietaryInfoRepository::class.java)
        dietaryInfoController = DietaryInfoController(dietaryInfoRepository)
    }

    @Test
    fun `getDietaryInfo should return list of dietary info`() = runTest {
        // Arrange
        val dietaryInfoDtoList = listOf(
            DietaryInfoDto(
                id = "1",
                name = "Gluten-Free",
                description = "Foods that do not contain gluten."
            ),
            DietaryInfoDto(
                id = "2",
                name = "Vegetarian",
                description = "Plant-based diet excluding meat."
            )
        )

        `when`(dietaryInfoRepository.getDietaryInfoList()).thenReturn(dietaryInfoDtoList)

        // Act
        val result = dietaryInfoController.getDietaryInfo()

        // Assert
        assertEquals(dietaryInfoDtoList.map { it.toDomain() }, result)
    }

    @Test
    fun `getDietaryInfo should return empty list on repository error`() = runTest {
        // Arrange
        `when`(dietaryInfoRepository.getDietaryInfoList()).thenThrow(RuntimeException("Simulated repository error"))

        // Act
        val result = dietaryInfoController.getDietaryInfo()

        // Assert
        assertEquals(emptyList<DietaryInfo>(), result)
    }
}
