package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.CuisineDto

data class Cuisine (
    var id: String = "",
    var name: String = "",
    var description: String? = ""
)

fun Cuisine.toDto(): CuisineDto {
    return CuisineDto(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

fun CuisineDto.toDomain(): Cuisine {
    return Cuisine(
        id = this.id,
        name = this.name,
        description = this.description
    )
}