package com.buildcal.probuild.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val title: String,
    val inputData: String,
    val resultData: String,
    val projectId: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
