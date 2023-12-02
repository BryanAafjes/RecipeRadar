package com.bth.reciperadar.data.dtos

data class RecipeDto(
    var id: String = "",
    var title: String = "",
    var description: String? = "",
    var userId: String? = "",
    var picturePath: String? = "",
    var prepTime: String? = "",
    var servingAmount: Int? = 0,
    var cuisines: List<CuisineDto>? = emptyList(),
    var reviews: List<ReviewDto>? = emptyList(),
    var steps: List<StepDto>? = emptyList(),
    var dietaryInfo: List<DietaryInfoDto>? = emptyList(),
    var ingredients: List<IngredientDto>? = emptyList()
)