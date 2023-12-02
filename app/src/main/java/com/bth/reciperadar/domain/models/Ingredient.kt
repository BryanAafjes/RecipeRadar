package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.IngredientDto

data class Ingredient (
    var id: String,
    var title: String,
    var ingredientTypes: List<IngredientType>,
    var amount: String,
)

fun Ingredient.toDto(): IngredientDto {
    return IngredientDto(
        id = this.id,
        title = this.title,
        ingredientTypes = this.ingredientTypes.map { it.toDto() },
        amount = this.amount
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = this.id,
        title = this.title,
        ingredientTypes = this.ingredientTypes.map { it.toDomain() },
        amount = this.amount
    )
}