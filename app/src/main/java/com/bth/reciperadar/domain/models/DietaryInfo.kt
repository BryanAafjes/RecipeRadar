package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.DietaryInfoDto

data class DietaryInfo (
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var description: String? = ""
)

fun DietaryInfo.toDto(): DietaryInfoDto {
    return DietaryInfoDto(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

fun DietaryInfoDto.toDomain(): DietaryInfo {
    return DietaryInfo(
        id = this.id,
        name = this.name,
        description = this.description
    )
}