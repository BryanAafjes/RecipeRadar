package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Recipe

data class RecipeViewModel(
    var id: String,
    var title: String,
    var description: String?,
    var userId: String?,
    var picturePath: String?,
    var prepTime: String?,
    var servingAmount: Int?,
    var cuisines: List<CuisineViewModel>?,
    var reviews: List<ReviewViewModel>?,
    var steps: List<StepViewModel>?,
    var dietaryInfo: List<DietaryInfoViewModel>?,
    var ingredients: List<IngredientViewModel>?
)

fun Recipe.toViewModel(): RecipeViewModel {
    return RecipeViewModel(
        id = this.id,
        title = this.title,
        description = this.description,
        userId = this.userId,
        picturePath = this.picturePath,
        prepTime = this.prepTime,
        servingAmount = this.servingAmount,
        cuisines = this.cuisines?.map { it.toViewModel() },
        reviews = this.reviews?.map { it.toViewModel() },
        steps = this.steps?.map { it.toViewModel() },
        dietaryInfo = this.dietaryInfo?.map { it.toViewModel() },
        ingredients = this.ingredients?.map { it.toViewModel() }
    )
}

fun RecipeViewModel.toDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        description = this.description,
        userId = this.userId,
        picturePath = this.picturePath,
        prepTime = this.prepTime,
        servingAmount = this.servingAmount,
        cuisines = this.cuisines?.map { it.toDomain() },
        reviews = this.reviews?.map { it.toDomain() },
        steps = this.steps?.map { it.toDomain() },
        dietaryInfo = this.dietaryInfo?.map { it.toDomain() },
        ingredients = this.ingredients?.map { it.toDomain() }
    )
}