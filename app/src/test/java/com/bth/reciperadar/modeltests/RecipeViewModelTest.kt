package com.bth.reciperadar.modeltests

import com.bth.reciperadar.domain.enums.ReviewRatingEnum
import com.bth.reciperadar.domain.models.Cuisine
import com.bth.reciperadar.domain.models.DietaryInfo
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.Recipe
import com.bth.reciperadar.domain.models.Review
import com.bth.reciperadar.domain.models.Step
import com.bth.reciperadar.presentation.viewmodels.CuisineViewModel
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.ReviewViewModel
import com.bth.reciperadar.presentation.viewmodels.StepViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import org.junit.Test
import org.junit.Assert.assertEquals

class RecipeViewModelTest {
    @Test
    fun `Recipe to RecipeViewModel conversion should be correct`() {
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
        val recipeViewModel = recipe.toViewModel()

        // Assert
        assertEquals("1", recipeViewModel.id)
        assertEquals("Test Recipe", recipeViewModel.title)
        assertEquals("This is a test recipe", recipeViewModel.description)
        assertEquals("user123", recipeViewModel.userId)
        assertEquals("/images/test_recipe.jpg", recipeViewModel.picturePath)
        assertEquals("30 minutes", recipeViewModel.prepTime)
        assertEquals(4, recipeViewModel.servingAmount)
        assertEquals(1, recipeViewModel.cuisines?.size)
        assertEquals("Italian", recipeViewModel.cuisines?.get(0)?.name)
        assertEquals(1, recipeViewModel.reviews?.size)
        assertEquals("review1", recipeViewModel.reviews?.get(0)?.id)
        assertEquals(1, recipeViewModel.steps?.size)
        assertEquals("Step 1", recipeViewModel.steps?.get(0)?.title)
        assertEquals(1, recipeViewModel.dietaryInfo?.size)
        assertEquals("vegan", recipeViewModel.dietaryInfo?.get(0)?.id)
        assertEquals(1, recipeViewModel.ingredients?.size)
        assertEquals("ingredient1", recipeViewModel.ingredients?.get(0)?.id)
    }

    @Test
    fun `RecipeViewModel to Recipe conversion should be correct`() {
        // Arrange
        val recipeViewModel = RecipeViewModel(
            id = "1",
            title = "Test Recipe",
            description = "This is a test recipe",
            userId = "user123",
            picturePath = "/images/test_recipe.jpg",
            prepTime = "30 minutes",
            servingAmount = 4,
            cuisines = listOf(CuisineViewModel(id = "1", name = "Italian", description = "")),
            reviews = listOf(ReviewViewModel(id = "review1", rating = ReviewRatingEnum.FiveStars, userId = "")),
            steps = listOf(StepViewModel(title = "Step 1", description = "", number = 1, picturePath = "")),
            dietaryInfo = listOf(DietaryInfoViewModel(id = "vegan", name = "Vegan", description = "")),
            ingredients = listOf(IngredientViewModel(id = "ingredient1", name = "Tomato", description = "", amount = "", ingredientType = null))
        )

        // Act
        val recipe = recipeViewModel.toDomain()

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