package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Cuisine

data class CuisineViewModel (
    var id: String,
    var title: String,
    var description: String,
)

fun CuisineViewModel.toDomain(): Cuisine {
    return Cuisine(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun Cuisine.toViewModel(): CuisineViewModel {
    return CuisineViewModel(
        id = this.id,
        title = this.title,
        description = this.description
    )
}