package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Cuisine
import kotlinx.coroutines.flow.Flow

@Dao
interface CuisineDao {
    @Upsert
    suspend fun upsertCuisine(cuisine: Cuisine)

    @Delete
    suspend fun deleteCuisine(cuisine: Cuisine)

    @Query("SELECT * FROM cuisine ORDER BY name ASC")
    fun getCuisineOrderedByName(): Flow<List<Cuisine>>
}