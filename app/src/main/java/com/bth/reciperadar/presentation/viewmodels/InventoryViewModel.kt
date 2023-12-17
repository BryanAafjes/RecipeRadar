package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Inventory

data class InventoryViewModel(
    var id: String = "",
    var userId: String = "",
    var ingredients: List<IngredientViewModel> = emptyList(),
)

fun Inventory.toViewModel(): InventoryViewModel {
    return InventoryViewModel(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toViewModel() },
    )
}

fun InventoryViewModel.toDomain(): Inventory {
    return Inventory(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDomain() },
    )
}