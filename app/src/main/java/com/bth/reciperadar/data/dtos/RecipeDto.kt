package com.bth.reciperadar.data.dtos

data class RecipeDto(
    var id: String,
    var title: String,
    var description: String,
    var userId: String,
    var picturePath: String,
    var prepTime: String,
    var servingAmount: Int,
    var cuisines: List<CuisineDto>,
    var reviews: List<ReviewDto>,
    var steps: List<StepDto>,
    var dietaryInfo: List<DietaryInfoDto>,
    var ingredients: List<IngredientDto>
)