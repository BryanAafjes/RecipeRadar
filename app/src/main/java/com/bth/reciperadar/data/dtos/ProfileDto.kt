package com.bth.reciperadar.data.dtos

data class ProfileDto(
    var id: String = "",
    var userId: String = "",
    var username: String = "",
    var email: String? = "",
    var picturePath: String? = "",
    var dietaryInfo: List<DietaryInfoDto> = emptyList(),
)