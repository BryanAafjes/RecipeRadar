package com.bth.reciperadar.domain.models

data class Recipe(
    var id: String,
    var title: String,
    var description: String,
    var userId: String,
    var picturePath: String,
    var prepTime: String,
    var servingAmount: Int,
    var cuisines: List<Cuisine>,
    var reviews: List<Review>,
    var steps: List<Step>,
    var dietaryInfo: List<DietaryInfo>,
    var ingredients: List<Ingredient>
)