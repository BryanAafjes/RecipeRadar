package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.RecipeDto

data class Recipe(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var title: String = "",
    var description: String? = "",
    var userId: String? = "",
    var picturePath: String? = "",
    var prepTime: String? = "",
    var servingAmount: Int? = 0,
    var cuisines: List<Cuisine>? = emptyList(),
    var reviews: List<Review>? = emptyList(),
    var steps: List<Step>? = emptyList(),
    var dietaryInfo: List<DietaryInfo>? = emptyList(),
    var ingredients: List<Ingredient>? = emptyList()
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
        cuisines = this.cuisines?.map { it.toDto() },
        reviews = this.reviews?.map { it.toDto() },
        steps = this.steps?.map { it.toDto() },
        dietaryInfo = this.dietaryInfo?.map { it.toDto() },
        ingredients = this.ingredients?.map { it.toDto() },
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
        cuisines = this.cuisines?.map { it.toDomain() },
        reviews = this.reviews?.map { it.toDomain() },
        steps = this.steps?.map { it.toDomain() },
        dietaryInfo = this.dietaryInfo?.map { it.toDomain() },
        ingredients = this.ingredients?.map { it.toDomain() },
    )
}