package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.ReviewRatingDto

data class ReviewRating (
    var id: String,
    var name: String,
)

fun ReviewRating.toDto(): ReviewRatingDto {
    return ReviewRatingDto(
        id = this.id,
        name = this.name
    )
}

fun ReviewRatingDto.toDomain(): ReviewRating {
    return ReviewRating(
        id = this.id,
        name = this.name
    )
}