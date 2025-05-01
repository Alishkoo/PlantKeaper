package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CareEventDao
import com.example.data.local.dao.PlantDao
import com.example.data.local.dao.ReminderDao
import com.example.data.local.entity.CareEventEntity
import com.example.data.local.entity.PlantEntity
import com.example.data.local.entity.ReminderEntity

@Database(
    entities = [
        PlantEntity::class,
        CareEventEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun careEventDao(): CareEventDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        private const val DATABASE_NAME = "plant_checker.db"

        @Volatile
        private var INSTANCE: PlantDatabase? = null

        fun getInstance(context: Context): PlantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}