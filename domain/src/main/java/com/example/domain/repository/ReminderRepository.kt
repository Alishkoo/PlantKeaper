package com.example.domain.repository

import com.example.core.common.Result
import com.example.domain.model.Reminder
import kotlinx.coroutines.flow.Flow


interface ReminderRepository {

    fun getRemindersForPlant(plantId: String): Flow<Result<List<Reminder>>>


    suspend fun addReminder(reminder: Reminder): Result<String>


    suspend fun updateReminder(reminder: Reminder): Result<Unit>


    suspend fun deleteReminder(id: String): Result<Unit>


    suspend fun getDueReminders(): Result<List<Reminder>>
}