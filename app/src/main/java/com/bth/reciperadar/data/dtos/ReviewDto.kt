package com.bth.reciperadar.data.dtos

data class ReviewDto (
    var id: String = "",
    var userId: String? = "",
    var rating: Int = 5
)