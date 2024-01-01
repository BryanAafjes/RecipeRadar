package com.bth.reciperadar.domain.models

import androidx.room.PrimaryKey
import com.bth.reciperadar.data.dtos.InventoryDto
data class Inventory(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var userId: String = "",
    var ingredients: List<Ingredient> = emptyList(),
)

fun Inventory.toDto(): InventoryDto {
    return InventoryDto(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDto() },
    )
}

fun InventoryDto.toDomain(): Inventory {
    return Inventory(
        id = this.id,
        userId = this.userId,
        ingredients = this.ingredients.map { it.toDomain() },
    )
}