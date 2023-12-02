package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Review

data class ReviewViewModel (
    var id: String,
    var recipe: RecipeViewModel?,
    var userId: String?,
    var rating: ReviewRatingViewModel,
)

fun Review.toViewModel(): ReviewViewModel {
    return ReviewViewModel(
        id = this.id,
        recipe = this.recipe?.toViewModel(),
        userId = this.userId,
        rating = this.rating.toViewModel()
    )
}

fun ReviewViewModel.toDomain(): Review {
    return Review(
        id = this.id,
        recipe = this.recipe?.toDomain(),
        userId = this.userId,
        rating = this.rating.toDomain()
    )
}