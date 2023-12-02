package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.IngredientDto

data class Ingredient (
    var id: String = "",
    var name: String = "",
    var ingredientTypes: List<IngredientType>? = emptyList(),
    var amount: String? = ""
)

fun Ingredient.toDto(): IngredientDto {
    return IngredientDto(
        id = this.id,
        name = this.name,
        ingredientTypes = this.ingredientTypes?.map { it.toDto() },
        amount = this.amount
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = this.id,
        name = this.name,
        ingredientTypes = this.ingredientTypes?.map { it.toDomain() },
        amount = this.amount
    )
}