package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Upsert
    suspend fun upsertProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("SELECT * FROM profile ORDER BY userId ASC")
    fun getProfileOrderedByUserId(): Flow<List<Profile>>
}