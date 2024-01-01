package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.ReviewDao
import com.bth.reciperadar.domain.models.Review

@Database(
    entities = [Review::class],
    version = 1
)
abstract class ReviewDatabase: RoomDatabase() {
    abstract val dao: ReviewDao;
}
