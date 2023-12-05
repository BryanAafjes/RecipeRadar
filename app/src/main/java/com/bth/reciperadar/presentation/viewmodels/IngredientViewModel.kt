package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.IngredientType

data class IngredientViewModel(
    var id: String,
    var name: String,
    var description: String,
    var ingredientType: IngredientType?,
    var amount: String?,
)

fun Ingredient.toViewModel(): IngredientViewModel {
    return IngredientViewModel(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredientType = this.ingredientType,
        amount = this.amount
    )
}

fun IngredientViewModel.toDomain(): Ingredient {
    return Ingredient(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredientType = this.ingredientType,
        amount = this.amount
    )
}