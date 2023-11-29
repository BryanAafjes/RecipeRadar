package com.bth.reciperadar.domain.models

data class Review (
    var id: String,
    var recipe: Recipe,
    var userId: String,
    var rating: ReviewRating,
)