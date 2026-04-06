package com.buildcal.probuild.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class MeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val length: Double,
    val width: Double,
    val height: Double = 0.0,
    val unit: String = "m",
    val notes: String = "",
    val projectId: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
