package com.bth.reciperadar.data.dtos

data class IngredientDto(
    var id: String = "",
    var name: String = "",
    var description: String? = "",
    var ingredientType: IngredientTypeDto? = null,
    var amount: String? = ""
)