package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE plantId = :plantId ORDER BY triggerTime ASC")
    fun getRemindersForPlant(plantId: String): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: String)

    @Query("SELECT * FROM reminders WHERE triggerTime <= :currentTime AND isScheduled = 1")
    suspend fun getDueReminders(currentTime: Long = System.currentTimeMillis()): List<ReminderEntity>
}