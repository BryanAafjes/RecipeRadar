package com.bth.reciperadar.data.dtos

data class StepDto (
    var title: String = "",
    var description: String? = "",
    var number: Int? = 0,
    var picturePath: String? = ""
)