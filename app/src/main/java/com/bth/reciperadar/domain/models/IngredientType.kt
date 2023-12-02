package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.IngredientTypeDto

data class IngredientType (
    var id: String,
    var name: String,
)

fun IngredientType.toDto(): IngredientTypeDto {
    return IngredientTypeDto(
        id = this.id,
        name = this.name
    )
}

fun IngredientTypeDto.toDomain(): IngredientType {
    return IngredientType(
        id = this.id,
        name = this.name
    )
}