package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.CareEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CareEventDao {
    @Query("SELECT * FROM care_events WHERE plantId = :plantId ORDER BY timestamp DESC")
    fun getCareEventsForPlant(plantId: String): Flow<List<CareEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareEvent(careEvent: CareEventEntity): Long

    @Delete
    suspend fun deleteCareEvent(careEvent: CareEventEntity)

    @Query("DELETE FROM care_events WHERE id = :id")
    suspend fun deleteCareEventById(id: String)

    @Query("SELECT * FROM care_events WHERE plantId = :plantId AND careType = :careType ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastCareEventByType(plantId: String, careType: String): CareEventEntity?
}