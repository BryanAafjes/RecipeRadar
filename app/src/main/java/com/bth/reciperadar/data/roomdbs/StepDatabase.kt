package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.StepDao
import com.bth.reciperadar.domain.models.Step

@Database(
    entities = [Step::class],
    version = 1
)
abstract class StepDatabase: RoomDatabase() {
    abstract val dao: StepDao;
}