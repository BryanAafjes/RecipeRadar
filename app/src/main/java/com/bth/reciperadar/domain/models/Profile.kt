package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.ProfileDto

data class Profile(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var userId: String = "",
    var username: String = "",
    var email: String? = "",
    var picturePath: String? = "",
    var pictureDownloadUri: String? = null,
    var dietaryInfo: List<DietaryInfo> = emptyList(),
)

fun Profile.toDto(): ProfileDto {
    return ProfileDto(
        id = this.id,
        userId = this.userId,
        username = this.username,
        email = this.email,
        picturePath = this.picturePath,
        dietaryInfo = this.dietaryInfo.map { it.toDto() },
    )
}

fun ProfileDto.toDomain(): Profile {
    return Profile(
        id = this.id,
        userId = this.userId,
        username = this.username,
        email = this.email,
        picturePath = this.picturePath,
        dietaryInfo = this.dietaryInfo.map { it.toDomain() },
    )
}