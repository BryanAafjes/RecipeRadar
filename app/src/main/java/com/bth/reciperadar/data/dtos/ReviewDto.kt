package com.bth.reciperadar.data.dtos

data class ReviewDto (
    var id: String = "",
    var recipe: RecipeDto = RecipeDto(),
    var userId: String = "",
    var rating: ReviewRatingDto = ReviewRatingDto()
)