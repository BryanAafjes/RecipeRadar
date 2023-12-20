package com.bth.reciperadar.domaintests

import com.bth.reciperadar.data.dtos.CuisineDto
import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.bth.reciperadar.data.repositories.RecipeRepository
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.domain.models.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.RuntimeException

class RecipeControllerUnitTests {
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeController: RecipeController

    @Before
    fun setUp() {
        recipeRepository = mock(RecipeRepository::class.java)
        recipeController = RecipeController(recipeRepository)
    }

    @Test
    fun `getRecipes should return list of recipes`() = runBlocking {
        // Arrange
        val recipeDtoList = listOf(RecipeDto(id = "1", title = "Recipe 1"), RecipeDto(id = "2", title = "Recipe 2"))
        `when`(recipeRepository.getRecipesWithoutReferences()).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.getRecipes()

        // Assert
        assertEquals(2, result.size)
        assertEquals("Recipe 1", result[0].title)
        assertEquals("Recipe 2", result[1].title)
    }

    @Test
    fun `getRecipeById should return recipe for valid id`() = runBlocking {
        // Arrange
        val recipeId = "1"
        val recipeDto = RecipeDto(id = recipeId, title = "Recipe 1")
        `when`(recipeRepository.getRecipeById(recipeId, true, true)).thenReturn(recipeDto)

        // Act
        val result = recipeController.getRecipeById(recipeId)

        // Assert
        assertEquals("Recipe 1", result?.title)
    }

    @Test
    fun `getRecipeById should return null for invalid id`() = runBlocking {
        // Arrange
        val recipeId = "invalidId"
        `when`(recipeRepository.getRecipeById(anyString(), anyBoolean(), anyBoolean())).thenReturn(null)

        // Act
        val result = recipeController.getRecipeById(recipeId)

        // Assert
        assertEquals(null, result)
    }

    @Test
    fun `getRecipeById should handle repository exception`() = runBlocking {
        // Arrange
        val recipeId = "1"
        `when`(recipeRepository.getRecipeById(recipeId, true, true)).thenThrow(RuntimeException("Repository error"))

        // Act
        val result = recipeController.getRecipeById(recipeId)

        // Assert
        assertEquals(null, result)
    }

    @Test
    fun `searchRecipes should return list of recipes`() = runBlocking {
        // Arrange
        val searchQuery = "chicken"
        val searchWords = listOf("chicken")
        val recipeDtoList = listOf(RecipeDto(id = "1", title = "Chicken Curry"), RecipeDto(id = "2", title = "Grilled Chicken"))
        `when`(recipeRepository.searchRecipesByTitle(eq(searchWords), eq(false))).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipes(searchQuery)

        // Assert
        assertEquals(2, result.size)
        assertEquals("Chicken Curry", result[0].title)
        assertEquals("Grilled Chicken", result[1].title)
    }

    @Test
    fun `searchRecipesByTitleAndFilters should return filtered recipes`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = listOf(Ingredient(id = "1", name = "Tomato"), Ingredient(id = "2", name = "Pasta"))
        val cuisinesList = listOf(Cuisine(id = "1", name = "Italian"))
        val dietaryInfoList = listOf(DietaryInfo(id = "1", name = "Vegetarian"))

        // Ensure that the cuisines in the recipeDtoList match the expected cuisinesList
        val recipeDtoList = listOf(
            RecipeDto(
                id = "1",
                title = "Vegetarian Pasta",
                cuisines = cuisinesList.map { it.toDto() },
                dietaryInfo = listOf(
                    DietaryInfoDto(id = "1", name = "Vegetarian")
                ),
                ingredients = listOf(IngredientDto(id = "2", name = "Pasta"))
            ),
            RecipeDto(
                id = "2",
                title = "Tomato Sauce Pasta",
                cuisines = cuisinesList.map { it.toDto() },
                ingredients = listOf(
                    IngredientDto(id = "1", name = "Tomato")
                )
            )
        )

        `when`(recipeRepository.searchRecipesByTitle(listOf("pasta"), true)).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = true,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(1, result.size)
        assertEquals("Vegetarian Pasta", result[0].title)
    }


    @Test
    fun `searchRecipesByTitleAndFilters should handle repository exception`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = listOf(Ingredient(id = "1", name = "Tomato"))
        val cuisinesList = emptyList<Cuisine>()
        val dietaryInfoList = emptyList<DietaryInfo>()
        `when`(recipeRepository.searchRecipesByTitle(eq(listOf("pasta")), eq(true))).thenThrow(RuntimeException("Repository error"))

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = false,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(emptyList<Recipe>(), result)
    }

    @Test
    fun `searchRecipesByTitleAndFilters should return recipes with selected ingredients`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = listOf(Ingredient(id = "1", name = "Tomato"), Ingredient(id = "2", name = "Pasta"))
        val cuisinesList = emptyList<Cuisine>()
        val dietaryInfoList = emptyList<DietaryInfo>()
        val recipeDtoList = listOf(
            RecipeDto(id = "1", title = "Vegetarian Pasta", ingredients = listOf(IngredientDto(id = "2", name = "Pasta"))),
            RecipeDto(id = "2", title = "Tomato Sauce Pasta", ingredients = listOf(IngredientDto(id = "1", name = "Tomato")))
        )
        `when`(recipeRepository.searchRecipesByTitle(listOf(searchQuery), true)).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = true,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(2, result.size)
        assertEquals("Vegetarian Pasta", result[0].title)
        assertEquals("Tomato Sauce Pasta", result[1].title)
    }

    @Test
    fun `searchRecipesByTitleAndFilters should return recipes without extra ingredients`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = listOf(Ingredient(id = "1", name = "Tomato"))
        val cuisinesList = emptyList<Cuisine>()
        val dietaryInfoList = emptyList<DietaryInfo>()
        val recipeDtoList = listOf(
            RecipeDto(id = "1", title = "Vegetarian Pasta", ingredients = listOf(IngredientDto(id = "1", name = "Tomato"), IngredientDto(id = "2", name = "Pasta"))),
            RecipeDto(id = "2", title = "Tomato Sauce Pasta", ingredients = listOf(IngredientDto(id = "1", name = "Tomato")))
        )
        `when`(recipeRepository.searchRecipesByTitle(eq(listOf("pasta")), eq(true))).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = false,
            dontAllowExtraIngredients = true
        )

        // Assert
        assertEquals(1, result.size)
        assertEquals("Tomato Sauce Pasta", result[0].title)
    }

    @Test
    fun `searchRecipesByTitleAndFilters should return recipes with optional ingredients`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = listOf(Ingredient(id = "1", name = "Tomato"))
        val cuisinesList = emptyList<Cuisine>()
        val dietaryInfoList = emptyList<DietaryInfo>()
        val recipeDtoList = listOf(
            RecipeDto(id = "1", title = "Vegetarian Pasta", ingredients = listOf(IngredientDto(id = "1", name = "Tomato"), IngredientDto(id = "2", name = "Pasta"))),
            RecipeDto(id = "2", title = "Tomato Sauce Pasta", ingredients = listOf(IngredientDto(id = "1", name = "Tomato")))
        )
        `when`(recipeRepository.searchRecipesByTitle(listOf(searchQuery), true)).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = false,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(2, result.size)
        assertEquals("Vegetarian Pasta", result[0].title)
        assertEquals("Tomato Sauce Pasta", result[1].title)
    }


    @Test
    fun `searchRecipesByTitleAndFilters should return recipes with selected cuisines`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = emptyList<Ingredient>()
        val cuisinesList = listOf(Cuisine(id = "1", name = "Italian"))
        val dietaryInfoList = emptyList<DietaryInfo>()
        val recipeDtoList = listOf(
            RecipeDto(id = "1", title = "Vegetarian Pasta", cuisines = listOf(CuisineDto(id = "1", name = "Italian"))),
            RecipeDto(id = "2", title = "Tomato Sauce Pasta", cuisines = listOf(CuisineDto(id = "1", name = "Italian")))
        )
        `when`(recipeRepository.searchRecipesByTitle(eq(listOf("pasta")), eq(true))).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = false,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(2, result.size)
        assertEquals("Vegetarian Pasta", result[0].title)
        assertEquals("Tomato Sauce Pasta", result[1].title)
    }

    @Test
    fun `searchRecipesByTitleAndFilters should return recipes with selected dietary info`() = runBlocking {
        // Arrange
        val searchQuery = "pasta"
        val ingredientsList = emptyList<Ingredient>()
        val cuisinesList = emptyList<Cuisine>()
        val dietaryInfoList = listOf(DietaryInfo(id = "1", name = "Vegetarian"))
        val recipeDtoList = listOf(
            RecipeDto(id = "1", title = "Vegetarian Pasta", dietaryInfo = listOf(DietaryInfoDto(id = "1", name = "Vegetarian"))),
            RecipeDto(id = "2", title = "Tomato Sauce Pasta", dietaryInfo = listOf(DietaryInfoDto(id = "1", name = "Vegetarian")))
        )
        `when`(recipeRepository.searchRecipesByTitle(eq(listOf("pasta")), eq(true))).thenReturn(recipeDtoList)

        // Act
        val result = recipeController.searchRecipesByTitleAndFilters(
            searchQuery = searchQuery,
            ingredientsList = ingredientsList,
            cuisinesList = cuisinesList,
            dietaryInfoList = dietaryInfoList,
            anyRecipesWithSelectedIngredients = false,
            dontAllowExtraIngredients = false
        )

        // Assert
        assertEquals(2, result.size)
        assertEquals("Vegetarian Pasta", result[0].title)
        assertEquals("Tomato Sauce Pasta", result[1].title)
    }
}
