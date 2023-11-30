package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.RecipeDto

data class Recipe(
    var id: String,
    var title: String,
    var description: String,
    var userId: String,
    var picturePath: String,
    var prepTime: String,
    var servingAmount: Int,
    var cuisines: List<Cuisine>,
    var reviews: List<Review>,
    var steps: List<Step>,
    var dietaryInfo: List<DietaryInfo>,
    var ingredients: List<Ingredient>
)

fun Recipe.toDto(): RecipeDto {
    return RecipeDto(
        id = this.id,
        title = this.title,
        description = this.description,
        userId = this.userId,
        picturePath = this.picturePath,
        prepTime = this.prepTime,
        servingAmount = this.servingAmount,
        cuisines = this.cuisines.map { it.toDto() },
        reviews = this.reviews.map { it.toDto() },
        steps = this.steps.map { it.toDto() },
        dietaryInfo = this.dietaryInfo.map { it.toDto() },
        ingredients = this.ingredients.map { it.toDto() },
    )
}

fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        description = this.description,
        userId = this.userId,
        picturePath = this.picturePath,
        prepTime = this.prepTime,
        servingAmount = this.servingAmount,
        cuisines = this.cuisines.map { it.toDomain() },
        reviews = this.reviews.map { it.toDomain() },
        steps = this.steps.map { it.toDomain() },
        dietaryInfo = this.dietaryInfo.map { it.toDomain() },
        ingredients = this.ingredients.map { it.toDomain() },
    )
}