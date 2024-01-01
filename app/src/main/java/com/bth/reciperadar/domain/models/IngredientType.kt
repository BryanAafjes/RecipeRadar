package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.IngredientTypeDto

data class IngredientType (
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var ingredients: List<Ingredient>? = null
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