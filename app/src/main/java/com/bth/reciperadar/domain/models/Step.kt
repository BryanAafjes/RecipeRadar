package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.StepDto

data class Step (
    @PrimaryKey(autoGenerate = false)
    var title: String = "",
    var description: String? = "",
    var number: Int? = 0,
    var picturePath: String? = ""
)

fun Step.toDto(): StepDto {
    return StepDto(
        title = this.title,
        description = this.description,
        number = this.number,
        picturePath = this.picturePath
    )
}

fun StepDto.toDomain(): Step {
    return Step(
        title = this.title,
        description = this.description,
        number = this.number,
        picturePath = this.picturePath
    )
}