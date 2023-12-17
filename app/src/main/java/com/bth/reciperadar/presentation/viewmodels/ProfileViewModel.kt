package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Profile

data class ProfileViewModel(
    var id: String = "",
    var userId: String = "",
    var username: String = "",
    var email: String? = "",
    var picturePath: String? = "",
    var pictureDownloadUri: String? = null,
    var dietaryInfo: List<DietaryInfoViewModel> = emptyList(),
)

fun Profile.toViewModel(): ProfileViewModel {
    return ProfileViewModel(
        id = this.id,
        userId = this.userId,
        username = this.username,
        email = this.email,
        picturePath = this.picturePath,
        pictureDownloadUri = this.pictureDownloadUri,
        dietaryInfo = this.dietaryInfo.map { it.toViewModel() },
    )
}

fun ProfileViewModel.toDomain(): Profile {
    return Profile(
        id = this.id,
        userId = this.userId,
        username = this.username,
        email = this.email,
        picturePath = this.picturePath,
        dietaryInfo = this.dietaryInfo.map { it.toDomain() },
    )
}