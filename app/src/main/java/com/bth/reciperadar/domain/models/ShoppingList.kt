package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.ShoppingListDto

data class ShoppingList(
    var id: String = "",
    var userId: String = "",
    var ingredients: List<Ingredient> = emptyList(),
)

fun ShoppingList.toDto(): ShoppingListDto {
    return ShoppingListDto(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDto() },
    )
}

fun ShoppingListDto.toDomain(): ShoppingList {
    return ShoppingList(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDomain() },
    )
}