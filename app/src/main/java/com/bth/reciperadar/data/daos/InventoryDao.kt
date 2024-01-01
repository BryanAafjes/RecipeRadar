package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.Inventory
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Upsert
    suspend fun upsertInventory(inventory: Inventory)

    @Delete
    suspend fun deleteInventory(inventory: Inventory)

    @Query("SELECT * FROM inventory ORDER BY userId ASC")
    fun getInventoryOrderedByUserId(): Flow<List<Inventory>>
}
