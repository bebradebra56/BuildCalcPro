package com.buildcal.probuild.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buildcal.probuild.data.db.dao.*
import com.buildcal.probuild.data.db.entity.*

@Database(
    entities = [
        ProjectEntity::class,
        RoomEntity::class,
        MaterialEntity::class,
        ShoppingItemEntity::class,
        MeasurementEntity::class,
        TaskEntity::class,
        CalculationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun roomDao(): RoomDao
    abstract fun materialDao(): MaterialDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun taskDao(): TaskDao
    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "buildcalcpro.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
