package com.bth.reciperadar.data.dtos

data class ProfileDto(
    var id: String = "",
    var userId: String = "",
    var username: String = "",
    var email: String? = "",
    var picturePath: String? = "",
    var dietaryInfo: List<DietaryInfoDto> = emptyList(),
)

fun ProfileDto.toFirebaseMap(): Map<String, Any?> {
    val dietaryInfoReferences = dietaryInfo.map { it.id } ?: emptyList<String>()

    return mapOf(
        "user_id" to userId,
        "username" to username,
        "dietary_info_references" to dietaryInfoReferences,
        "picture_path" to picturePath
    )
}