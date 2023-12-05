package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.IngredientDto

data class Ingredient (
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var ingredientType: IngredientType? = null,
    var amount: String? = ""
)

fun Ingredient.toDto(): IngredientDto {
    return IngredientDto(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredientType = this.ingredientType,
        amount = this.amount
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredientType = this.ingredientType,
        amount = this.amount
    )
}