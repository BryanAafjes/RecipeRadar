package com.bth.reciperadar.data.dtos

import com.bth.reciperadar.domain.models.IngredientType

data class IngredientDto(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var ingredientType: IngredientType? = null,
    var amount: String? = ""
)