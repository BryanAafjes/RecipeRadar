package com.bth.reciperadar.domain.models

import com.bth.reciperadar.data.dtos.DietaryInfoDto

data class DietaryInfo (
    var id: String,
    var title: String,
    var description: String,
)

fun DietaryInfo.toDto(): DietaryInfoDto {
    return DietaryInfoDto(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun DietaryInfoDto.toDomain(): DietaryInfo {
    return DietaryInfo(
        id = this.id,
        title = this.title,
        description = this.description
    )
}