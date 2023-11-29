package com.bth.reciperadar.domain.models

data class Ingredient (
    var id: String,
    var title: String,
    var ingredientTypes: List<IngredientType>,
    var amount: String,
)