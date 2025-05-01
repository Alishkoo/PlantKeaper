package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "care_events",
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
data class CareEventEntity(
    @PrimaryKey val id: String,
    val plantId: String,
    val careType: String,
    val timestamp: Long,
    val notes: String,
    val isSyncedWithServer: Boolean = false
)