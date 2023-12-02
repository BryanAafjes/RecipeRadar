package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.DietaryInfo

data class DietaryInfoViewModel (
    var id: String,
    var title: String,
    var description: String,
)

fun DietaryInfoViewModel.toDomain(): DietaryInfo {
    return DietaryInfo(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun DietaryInfo.toViewModel(): DietaryInfoViewModel {
    return DietaryInfoViewModel(
        id = this.id,
        title = this.title,
        description = this.description
    )
}