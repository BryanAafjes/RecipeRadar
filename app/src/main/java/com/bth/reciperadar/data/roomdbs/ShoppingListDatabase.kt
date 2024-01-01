package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.ShoppingListDao
import com.bth.reciperadar.domain.models.ShoppingList

@Database(
    entities = [ShoppingList::class],
    version = 1
)
abstract class ShoppingListDatabase: RoomDatabase() {
    abstract val dao: ShoppingListDao;
}
