package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.ReviewRating

data class ReviewRatingViewModel (
    var id: String,
    var name: String,
)

fun ReviewRating.toViewModel(): ReviewRatingViewModel {
    return ReviewRatingViewModel(
        id = this.id,
        name = this.name
    )
}

fun ReviewRatingViewModel.toDomain(): ReviewRating {
    return ReviewRating(
        id = this.id,
        name = this.name
    )
}