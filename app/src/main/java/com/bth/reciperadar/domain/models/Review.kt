package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.ReviewDto
import com.bth.reciperadar.domain.enums.ReviewRatingEnum

data class Review (
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var userId: String? = "",
    var rating: ReviewRatingEnum = ReviewRatingEnum.FiveStars
)

fun Review.toDto(): ReviewDto {
    return ReviewDto(
        id = this.id,
        userId = this.userId,
        rating = this.rating.numericValue
    )
}

fun ReviewDto.toDomain(): Review {
    return Review(
        id = this.id,
        userId = this.userId,
        rating = ReviewRatingEnum.values().firstOrNull { it.numericValue == this.rating }
            ?: ReviewRatingEnum.FiveStars
    )
}