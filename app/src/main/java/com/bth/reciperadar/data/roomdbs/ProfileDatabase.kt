package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.ProfileDao
import com.bth.reciperadar.domain.models.Profile

@Database(
    entities = [Profile::class],
    version = 1
)
abstract class ProfileDatabase: RoomDatabase() {
    abstract val dao: ProfileDao;
}
