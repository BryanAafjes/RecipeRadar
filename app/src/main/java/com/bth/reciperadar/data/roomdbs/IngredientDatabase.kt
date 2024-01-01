package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.IngredientDao
import com.bth.reciperadar.domain.models.Ingredient

@Database(
    entities = [Ingredient::class],
    version = 1
)
abstract class IngredientDatabase: RoomDatabase() {
    abstract val dao: IngredientDao;
}