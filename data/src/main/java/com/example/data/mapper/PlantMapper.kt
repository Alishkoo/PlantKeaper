package com.example.data.mapper

import com.example.data.local.entity.PlantEntity
import com.example.domain.model.Plant
import java.util.UUID

object PlantMapper {
    fun mapToDomain(entity: PlantEntity): Plant {
        return Plant(
            id = entity.id,
            name = entity.name,
            species = entity.species,
            imageUrl = entity.imageUrl,
            wateringFrequencyDays = entity.wateringFrequencyDays,
            lastWateredTimestamp = entity.lastWateredTimestamp,
            nextWateringDue = entity.nextWateringDue,
            sunlightNeeds = entity.sunlightNeeds,
            soilType = entity.soilType,
            notes = entity.notes,
            isFavorite = entity.isFavorite
        )
    }

    fun mapToEntity(domain: Plant, isSyncedWithServer: Boolean = false): PlantEntity {
        return PlantEntity(
            id = domain.id.ifEmpty { UUID.randomUUID().toString() },
            name = domain.name,
            species = domain.species,
            imageUrl = domain.imageUrl.toString(),
            wateringFrequencyDays = domain.wateringFrequencyDays,
            lastWateredTimestamp = domain.lastWateredTimestamp,
            nextWateringDue = domain.nextWateringDue,
            sunlightNeeds = domain.sunlightNeeds.toString(),
            soilType = domain.soilType.toString(),
            notes = domain.notes.toString(),
            isFavorite = domain.isFavorite,
            isSyncedWithServer = isSyncedWithServer
        )
    }
}