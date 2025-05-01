package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("plantId")]
)
data class ReminderEntity(
    @PrimaryKey val id: String,
    val plantId: String,
    val title: String,
    val message: String,
    val triggerTime: Long,
    val isRecurring: Boolean,
    val recurrenceIntervalDays: Int,
    val isScheduled: Boolean = false
)