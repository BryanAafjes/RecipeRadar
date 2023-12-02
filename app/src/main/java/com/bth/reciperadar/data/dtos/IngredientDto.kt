package com.bth.reciperadar.data.dtos

data class IngredientDto (
    var id: String = "",
    var name: String = "",
    var ingredientTypes: List<IngredientTypeDto>? = emptyList(),
    var amount: String? = ""
)