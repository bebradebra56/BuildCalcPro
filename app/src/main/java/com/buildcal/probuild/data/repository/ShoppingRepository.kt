package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.ShoppingDao
import com.buildcal.probuild.data.db.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val dao: ShoppingDao) {
    fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>> = dao.getAllShoppingItems()
    fun getShoppingItemsByProject(projectId: Long): Flow<List<ShoppingItemEntity>> = dao.getShoppingItemsByProject(projectId)
    fun getPendingItemCount(): Flow<Int> = dao.getPendingItemCount()
    suspend fun getShoppingItemById(id: Long): ShoppingItemEntity? = dao.getShoppingItemById(id)
    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long = dao.insertShoppingItem(item)
    suspend fun updateShoppingItem(item: ShoppingItemEntity) = dao.updateShoppingItem(item)
    suspend fun deleteShoppingItem(item: ShoppingItemEntity) = dao.deleteShoppingItem(item)
    suspend fun setItemPurchased(id: Long, purchased: Boolean) = dao.setItemPurchased(id, purchased)
}
