package com.bth.reciperadar.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bth.reciperadar.domain.models.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Upsert
    suspend fun upsertShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)

    @Query("SELECT * FROM shoppinglist ORDER BY userId ASC")
    fun getShoppingListOrderedByUserId(): Flow<List<ShoppingList>>
}