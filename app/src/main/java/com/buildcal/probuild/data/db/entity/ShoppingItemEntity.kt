package com.buildcal.probuild.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Double,
    val unit: String,
    val isPurchased: Boolean = false,
    val notes: String = "",
    val projectId: Long = 0,
    val estimatedPrice: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
