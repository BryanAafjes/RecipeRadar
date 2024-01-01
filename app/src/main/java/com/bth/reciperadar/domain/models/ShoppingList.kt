package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.ShoppingListDto

data class ShoppingList(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var userId: String = "",
    var ingredients: List<Ingredient> = emptyList(),
    var checkedIngredients: List<Ingredient> = emptyList(),
)

fun ShoppingList.toDto(): ShoppingListDto {
    return ShoppingListDto(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDto() },
        checkedIngredients = this.checkedIngredients.map { it.toDto() }
    )
}

fun ShoppingListDto.toDomain(): ShoppingList {
    return ShoppingList(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDomain() },
        checkedIngredients = this.checkedIngredients.map { it.toDomain() },
    )
}