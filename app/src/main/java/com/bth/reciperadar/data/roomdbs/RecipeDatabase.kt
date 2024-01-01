package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.RecipeDao
import com.bth.reciperadar.domain.models.Recipe

@Database(
    entities = [Recipe::class],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {
    abstract val dao: RecipeDao;
}
