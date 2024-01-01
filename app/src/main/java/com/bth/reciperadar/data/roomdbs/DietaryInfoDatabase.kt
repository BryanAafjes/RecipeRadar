package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.DietaryInfoDao
import com.bth.reciperadar.domain.models.DietaryInfo

@Database(
    entities = [DietaryInfo::class],
    version = 1
)
abstract class DietaryInfoDatabase: RoomDatabase() {
    abstract val dao: DietaryInfoDao;
}