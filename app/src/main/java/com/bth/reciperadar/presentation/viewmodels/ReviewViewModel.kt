package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.enums.ReviewRatingEnum
import com.bth.reciperadar.domain.models.Review

data class ReviewViewModel (
    var id: String,
    var userId: String?,
    var rating: ReviewRatingEnum,
)

fun Review.toViewModel(): ReviewViewModel {
    return ReviewViewModel(
        id = this.id,
        userId = this.userId,
        rating = this.rating
    )
}

fun ReviewViewModel.toDomain(): Review {
    return Review(
        id = this.id,
        userId = this.userId,
        rating = this.rating
    )
}