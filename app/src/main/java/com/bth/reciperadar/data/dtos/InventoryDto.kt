package com.bth.reciperadar.data.dtos

data class InventoryDto(
    var id: String = "",
    var userId: String = "",
    var ingredients: List<IngredientDto> = emptyList(),
)