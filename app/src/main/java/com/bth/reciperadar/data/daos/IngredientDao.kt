package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Upsert
    suspend fun upsertIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient ORDER BY name ASC")
    fun getIngredientOrderedByName(): Flow<List<Ingredient>>
}