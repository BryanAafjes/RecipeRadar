package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.IngredientTypeDao
import com.bth.reciperadar.domain.models.IngredientType

@Database(
    entities = [IngredientType::class],
    version = 1
)
abstract class IngredientTypeDatabase: RoomDatabase() {
    abstract val dao: IngredientTypeDao;
}
