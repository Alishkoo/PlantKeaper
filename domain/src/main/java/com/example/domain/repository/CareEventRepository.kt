package com.example.domain.repository

import com.example.core.common.Result
import com.example.domain.model.CareEvent
import com.example.domain.model.CareType
import kotlinx.coroutines.flow.Flow


interface CareEventRepository {

    fun getCareEventsForPlant(plantId: String): Flow<Result<List<CareEvent>>>

    suspend fun addCareEvent(careEvent: CareEvent): Result<String>

    suspend fun deleteCareEvent(id: String): Result<Unit>

    suspend fun getLastCareEventByType(plantId: String, careType: CareType): Result<CareEvent?>
}