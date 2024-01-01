package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.CuisineDao
import com.bth.reciperadar.domain.models.Cuisine

@Database(
    entities = [Cuisine::class],
    version = 1
)
abstract class CuisineDatabase: RoomDatabase() {
    abstract val dao: CuisineDao;
}