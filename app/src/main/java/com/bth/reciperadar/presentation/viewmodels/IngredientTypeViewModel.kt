package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.IngredientType

data class IngredientTypeViewModel (
    var id: String,
    var name: String,
    var ingredients: List<IngredientViewModel>?
)

fun IngredientTypeViewModel.toDomain(): IngredientType {
    return IngredientType(
        id = this.id,
        name = this.name,
        ingredients = this.ingredients?.map { it.toDomain() },
    )
}

fun IngredientType.toViewModel(): IngredientTypeViewModel {
    return IngredientTypeViewModel(
        id = this.id,
        name = this.name,
        ingredients = this.ingredients?.map { it.toViewModel() },
    )
}