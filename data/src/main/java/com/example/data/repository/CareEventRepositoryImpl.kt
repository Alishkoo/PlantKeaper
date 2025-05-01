package com.example.data.repository

import com.example.core.common.Result
import com.example.core.extensions.asResult
import com.example.data.local.dao.CareEventDao
import com.example.data.mapper.CareEventMapper
import com.example.domain.model.CareEvent
import com.example.domain.model.CareType
import com.example.domain.repository.CareEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CareEventRepositoryImpl(
    private val careEventDao: CareEventDao
) : CareEventRepository {

    override fun getCareEventsForPlant(plantId: String): Flow<Result<List<CareEvent>>> {
        return careEventDao.getCareEventsForPlant(plantId)
            .map { entities -> entities.map { CareEventMapper.mapToDomain(it) } }
            .asResult()
    }

    override suspend fun addCareEvent(careEvent: CareEvent): Result<String> {
        return try {
            val id = careEvent.id.ifEmpty { UUID.randomUUID().toString() }
            val eventToSave = if (careEvent.id.isEmpty()) careEvent.copy(id = id) else careEvent

            careEventDao.insertCareEvent(CareEventMapper.mapToEntity(eventToSave))
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteCareEvent(id: String): Result<Unit> {
        return try {
            careEventDao.deleteCareEventById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getLastCareEventByType(plantId: String, careType: CareType): Result<CareEvent?> {
        return try {
            val event = careEventDao.getLastCareEventByType(plantId, careType.name)
            if (event != null) {
                Result.Success(CareEventMapper.mapToDomain(event))
            } else {
                Result.Success(null)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}