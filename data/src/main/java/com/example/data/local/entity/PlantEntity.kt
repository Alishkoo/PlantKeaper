package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val species: String,
    val imageUrl: String,
    val wateringFrequencyDays: Int,
    val lastWateredTimestamp: Long,
    val nextWateringDue: Long,
    val sunlightNeeds: String,
    val soilType: String,
    val notes: String,
    val isFavorite: Boolean,
    val isSyncedWithServer: Boolean = false
)