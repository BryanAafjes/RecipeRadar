package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Step
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Upsert
    suspend fun upsertStep(step: Step)

    @Delete
    suspend fun deleteStep(step: Step)

    @Query("SELECT * FROM step ORDER BY title ASC")
    fun getStepOrderedByTitle(): Flow<List<Step>>
}