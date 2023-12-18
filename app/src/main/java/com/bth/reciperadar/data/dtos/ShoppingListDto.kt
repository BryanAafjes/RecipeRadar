package com.bth.reciperadar.data.dtos

data class ShoppingListDto(
    var id: String = "",
    var userId: String = "",
    var ingredients: List<IngredientDto> = emptyList(),
    var checkedIngredients: List<IngredientDto> = emptyList(),
)