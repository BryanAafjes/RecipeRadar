package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.IngredientType
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientTypeDao {
    @Upsert
    suspend fun upsertIngredientType(ingredientType: IngredientType)

    @Delete
    suspend fun deleteIngredientType(ingredientType: IngredientType)

    @Query("SELECT * FROM ingredienttype ORDER BY name ASC")
    fun getIngredientTypeOrderedByName(): Flow<List<IngredientType>>
}
