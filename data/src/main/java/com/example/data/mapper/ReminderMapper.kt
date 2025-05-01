package com.example.data.mapper

import com.example.data.local.entity.ReminderEntity
import com.example.domain.model.Reminder
import java.util.UUID

/**
 * Маппер для преобразования между доменной моделью Reminder и сущностью ReminderEntity
 */
object ReminderMapper {
    fun mapToDomain(entity: ReminderEntity): Reminder {
        return Reminder(
            id = entity.id,
            plantId = entity.plantId,
            title = entity.title,
            message = entity.message,
            triggerTime = entity.triggerTime,
            isRecurring = entity.isRecurring,
            recurrenceIntervalDays = entity.recurrenceIntervalDays
        )
    }

    fun mapToEntity(domain: Reminder, isScheduled: Boolean = false): ReminderEntity {
        return ReminderEntity(
            id = domain.id.ifEmpty { UUID.randomUUID().toString() },
            plantId = domain.plantId,
            title = domain.title,
            message = domain.message,
            triggerTime = domain.triggerTime,
            isRecurring = domain.isRecurring,
            recurrenceIntervalDays = domain.recurrenceIntervalDays,
            isScheduled = isScheduled
        )
    }
}