package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Cuisine

data class CuisineViewModel (
    var id: String,
    var name: String,
    var description: String?,
)

fun CuisineViewModel.toDomain(): Cuisine {
    return Cuisine(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

fun Cuisine.toViewModel(): CuisineViewModel {
    return CuisineViewModel(
        id = this.id,
        name = this.name,
        description = this.description
    )
}