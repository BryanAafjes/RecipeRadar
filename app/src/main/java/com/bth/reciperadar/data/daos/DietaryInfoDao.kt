package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.DietaryInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface DietaryInfoDao {
    @Upsert
    suspend fun upsertDietaryInfo(diet: DietaryInfo)

    @Delete
    suspend fun deleteDietaryInfo(diet: DietaryInfo)

    @Query("SELECT * FROM dietaryinfo ORDER BY name ASC")
    fun getDietaryInfoOrderedByName(): Flow<List<DietaryInfo>>
}
