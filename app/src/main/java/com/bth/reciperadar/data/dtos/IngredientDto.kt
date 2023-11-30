package com.bth.reciperadar.data.dtos

data class IngredientDto (
    var id: String,
    var title: String,
    var ingredientTypes: List<IngredientTypeDto>,
    var amount: String,
)