package com.buildcal.probuild.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materials")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val unit: String,
    val pricePerUnit: Double = 0.0,
    val quantity: Double = 0.0,
    val projectId: Long = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
