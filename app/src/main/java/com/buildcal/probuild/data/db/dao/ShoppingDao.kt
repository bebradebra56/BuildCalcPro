package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY isPurchased ASC, createdAt DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE projectId = :projectId ORDER BY isPurchased ASC, createdAt DESC")
    fun getShoppingItemsByProject(projectId: Long): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE id = :id")
    suspend fun getShoppingItemById(id: Long): ShoppingItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long

    @Update
    suspend fun updateShoppingItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItemEntity)

    @Query("SELECT COUNT(*) FROM shopping_items WHERE isPurchased = 0")
    fun getPendingItemCount(): Flow<Int>

    @Query("UPDATE shopping_items SET isPurchased = :purchased WHERE id = :id")
    suspend fun setItemPurchased(id: Long, purchased: Boolean)
}
