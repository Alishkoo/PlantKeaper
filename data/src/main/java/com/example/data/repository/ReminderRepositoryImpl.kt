package com.example.data.repository

import com.example.core.common.Result
import com.example.core.extensions.asResult
import com.example.data.local.dao.ReminderDao
import com.example.data.mapper.ReminderMapper
import com.example.domain.model.Reminder
import com.example.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getRemindersForPlant(plantId: String): Flow<Result<List<Reminder>>> {
        return reminderDao.getRemindersForPlant(plantId)
            .map { entities -> entities.map { ReminderMapper.mapToDomain(it) } }
            .asResult()
    }

    override suspend fun addReminder(reminder: Reminder): Result<String> {
        return try {
            val id = reminder.id.ifEmpty { UUID.randomUUID().toString() }
            val reminderToSave = if (reminder.id.isEmpty()) reminder.copy(id = id) else reminder

            reminderDao.insertReminder(ReminderMapper.mapToEntity(reminderToSave, isScheduled = true))
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Unit> {
        return try {
            reminderDao.updateReminder(ReminderMapper.mapToEntity(reminder, isScheduled = true))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteReminder(id: String): Result<Unit> {
        return try {
            reminderDao.deleteReminderById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getDueReminders(): Result<List<Reminder>> {
        return try {
            val dueReminders = reminderDao.getDueReminders()
            Result.Success(dueReminders.map { ReminderMapper.mapToDomain(it) })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}