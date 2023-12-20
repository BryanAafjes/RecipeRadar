package com.bth.reciperadar.modeltests

import com.bth.reciperadar.data.dtos.CuisineDto
import com.bth.reciperadar.data.dtos.DietaryInfoDto
import com.bth.reciperadar.data.dtos.IngredientDto
import com.bth.reciperadar.data.dtos.RecipeDto
import com.bth.reciperadar.data.dtos.ReviewDto
import com.bth.reciperadar.data.dtos.StepDto
import com.bth.reciperadar.domain.enums.ReviewRatingEnum
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.Recipe
import com.bth.reciperadar.domain.models.Review
import com.bth.reciperadar.domain.models.Step
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import org.junit.Test
import org.junit.Assert.assertEquals

class RecipeModelTest {
    @Test
    fun `Recipe to RecipeDto conversion should be correct`() {
        // Arrange
        val recipe = Recipe(
            id = "1",
            title = "Test Recipe",
            description = "This is a test recipe",
            userId = "user123",
            picturePath = "/images/test_recipe.jpg",
            prepTime = "30 minutes",
            servingAmount = 4,
            cuisines = listOf(Cuisine(id = "italian", name = "Italian")),
            reviews = listOf(Review(id = "review1", rating = ReviewRatingEnum.FiveStars)),
            steps = listOf(Step(title = "Step 1")),
            dietaryInfo = listOf(DietaryInfo(id = "vegan", name = "Vegan")),
            ingredients = listOf(Ingredient(id = "ingredient1", name = "Tomato"))
        )

        // Act
        val recipeDto = recipe.toDto()

        // Assert
        assertEquals("1", recipeDto.id)
        assertEquals("Test Recipe", recipeDto.title)
        assertEquals("This is a test recipe", recipeDto.description)
        assertEquals("user123", recipeDto.userId)
        assertEquals("/images/test_recipe.jpg", recipeDto.picturePath)
        assertEquals("30 minutes", recipeDto.prepTime)
        assertEquals(4, recipeDto.servingAmount)
        assertEquals(1, recipeDto.cuisines?.size)
        assertEquals("Italian", recipeDto.cuisines?.get(0)?.name)
        assertEquals(1, recipeDto.reviews?.size)
        assertEquals("review1", recipeDto.reviews?.get(0)?.id)
        assertEquals(1, recipeDto.steps?.size)
        assertEquals("Step 1", recipeDto.steps?.get(0)?.title)
        assertEquals(1, recipeDto.dietaryInfo?.size)
        assertEquals("vegan", recipeDto.dietaryInfo?.get(0)?.id)
        assertEquals(1, recipeDto.ingredients?.size)
        assertEquals("ingredient1", recipeDto.ingredients?.get(0)?.id)
    }

    @Test
    fun `RecipeDto to Recipe conversion should be correct`() {
        // Arrange
        val recipeDto = RecipeDto(
            id = "1",
            title = "Test Recipe",
            description = "This is a test recipe",
            userId = "user123",
            picturePath = "/images/test_recipe.jpg",
            prepTime = "30 minutes",
            servingAmount = 4,
            cuisines = listOf(CuisineDto(id = "italian", name = "Italian")),
            reviews = listOf(ReviewDto(id = "review1", rating = 5)),
            steps = listOf(StepDto(title = "Step 1")),
            dietaryInfo = listOf(DietaryInfoDto(id = "vegan", name = "Vegan")),
            ingredients = listOf(IngredientDto(id = "ingredient1", name = "Tomato"))
        )

        // Act
        val recipe = recipeDto.toDomain()

        // Assert
        assertEquals("1", recipe.id)
        assertEquals("Test Recipe", recipe.title)
        assertEquals("This is a test recipe", recipe.description)
        assertEquals("user123", recipe.userId)
        assertEquals("/images/test_recipe.jpg", recipe.picturePath)
        assertEquals("30 minutes", recipe.prepTime)
        assertEquals(4, recipe.servingAmount)
        assertEquals(1, recipe.cuisines?.size)
        assertEquals("Italian", recipe.cuisines?.get(0)?.name)
        assertEquals(1, recipe.reviews?.size)
        assertEquals("review1", recipe.reviews?.get(0)?.id)
        assertEquals(1, recipe.steps?.size)
        assertEquals("Step 1", recipe.steps?.get(0)?.title)
        assertEquals(1, recipe.dietaryInfo?.size)
        assertEquals("vegan", recipe.dietaryInfo?.get(0)?.id)
        assertEquals(1, recipe.ingredients?.size)
        assertEquals("ingredient1", recipe.ingredients?.get(0)?.id)
    }
}
