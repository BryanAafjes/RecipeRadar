package com.bth.reciperadar.data.roomdbs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bth.reciperadar.data.daos.InventoryDao
import com.bth.reciperadar.domain.models.Inventory

@Database(
    entities = [Inventory::class],
    version = 1
)
abstract class InventoryDatabase: RoomDatabase() {
    abstract val dao: InventoryDao;
}
