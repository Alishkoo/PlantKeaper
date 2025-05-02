package com.example.data.mapper

import com.example.data.local.entity.CareEventEntity
import com.example.domain.model.CareEvent
import com.example.domain.model.CareType
import java.util.UUID


object CareEventMapper {
    fun mapToDomain(entity: CareEventEntity): CareEvent {
        return CareEvent(
            id = entity.id,
            plantId = entity.plantId,
            careType = CareType.valueOf(entity.careType),
            timestamp = entity.timestamp,
            notes = entity.notes
        )
    }

    fun mapToEntity(domain: CareEvent, isSyncedWithServer: Boolean = false): CareEventEntity {
        return CareEventEntity(
            id = domain.id.ifEmpty { UUID.randomUUID().toString() },
            plantId = domain.plantId,
            careType = domain.careType.name,
            timestamp = domain.timestamp,
            notes = domain.notes,
            isSyncedWithServer = isSyncedWithServer
        )
    }
}