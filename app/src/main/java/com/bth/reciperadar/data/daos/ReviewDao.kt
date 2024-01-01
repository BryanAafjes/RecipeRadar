package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Upsert
    suspend fun upsertReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)

    @Query("SELECT * FROM review ORDER BY userId ASC")
    fun getReviewOrderedByUserId(): Flow<List<Review>>
}
