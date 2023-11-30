package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.ReviewDto

data class Review (
    var id: String,
    var recipe: Recipe,
    var userId: String,
    var rating: ReviewRating,
)

fun Review.toDto(): ReviewDto {
    return ReviewDto(
        id = this.id,
        recipe = this.recipe.toDto(),
        userId = this.userId,
        rating = this.rating.toDto()
    )
}

fun ReviewDto.toDomain(): Review {
    return Review(
        id = this.id,
        recipe = this.recipe.toDomain(),
        userId = this.userId,
        rating = this.rating.toDomain()
    )
}