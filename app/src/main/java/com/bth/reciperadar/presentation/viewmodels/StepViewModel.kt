package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.Step

data class StepViewModel (
    var title: String,
    var description: String?,
    var number: Int?,
    var picturePath: String?,
)

fun Step.toViewModel(): StepViewModel {
    return StepViewModel(
        title = this.title,
        description = this.description,
        number = this.number,
        picturePath = this.picturePath
    )
}

fun StepViewModel.toDomain(): Step {
    return Step(
        title = this.title,
        description = this.description,
        number = this.number,
        picturePath = this.picturePath
    )
}