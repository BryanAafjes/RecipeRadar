package com.bth.reciperadar.data.dtos

data class ReviewDto (
    var id: String,
    var recipe: RecipeDto,
    var userId: String,
    var rating: ReviewRatingDto,
)