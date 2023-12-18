package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.ShoppingList

data class ShoppingListViewModel(
    var id: String = "",
    var userId: String = "",
    var ingredients: List<IngredientViewModel> = emptyList(),
    var checkedIngredients: List<IngredientViewModel> = emptyList(),
)

fun ShoppingList.toViewModel(): ShoppingListViewModel {
    return ShoppingListViewModel(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toViewModel() },
        checkedIngredients = this.checkedIngredients.map { it.toViewModel() },
    )
}

fun ShoppingListViewModel.toDomain(): ShoppingList {
    return ShoppingList(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDomain() },
        checkedIngredients = this.checkedIngredients.map { it.toDomain() },
    )
}