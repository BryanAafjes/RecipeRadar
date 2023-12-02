package com.bth.reciperadar.presentation.viewmodels

import com.bth.reciperadar.domain.models.DietaryInfo

data class DietaryInfoViewModel (
    var id: String,
    var name: String,
    var description: String?,
)

fun DietaryInfoViewModel.toDomain(): DietaryInfo {
    return DietaryInfo(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

fun DietaryInfo.toViewModel(): DietaryInfoViewModel {
    return DietaryInfoViewModel(
        id = this.id,
        name = this.name,
        description = this.description
    )
}