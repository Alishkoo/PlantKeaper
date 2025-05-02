package com.example.data.repository

import com.example.core.common.Result
import com.example.core.extensions.asResult
import com.example.data.local.dao.PlantDao
import com.example.data.mapper.PlantMapper
import com.example.domain.model.Plant
import com.example.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class PlantRepositoryImpl(
    private val plantDao: PlantDao
) : PlantRepository {

    override fun getPlants(): Flow<Result<List<Plant>>> {
        return plantDao.getAllPlants()
            .map { entities -> entities.map { PlantMapper.mapToDomain(it) } }
            .asResult()
    }

    override suspend fun getPlantById(id: String): Result<Plant> {
        val plantEntity = plantDao.getPlantById(id) ?: return Result.Error(Exception("Plant not found"))
        return Result.Success(PlantMapper.mapToDomain(plantEntity))
    }

    override suspend fun addPlant(plant: Plant): Result<String> {
        try {
            val plantId = plant.id.ifEmpty { UUID.randomUUID().toString() }
            val plantToSave = if (plant.id.isEmpty()) plant.copy(id = plantId) else plant

            val plantWithNextWatering = if (plantToSave.nextWateringDue == 0L && plantToSave.lastWateredTimestamp > 0) {
                plantToSave.copy(
                    nextWateringDue = Plant.calculateNextWatering(
                        plantToSave.lastWateredTimestamp,
                        plantToSave.wateringFrequencyDays
                    )
                )
            } else {
                plantToSave
            }

            plantDao.insertPlant(PlantMapper.mapToEntity(plantWithNextWatering))
            return Result.Success(plantId)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun updatePlant(plant: Plant): Result<Unit> {
        return try {
            plantDao.updatePlant(PlantMapper.mapToEntity(plant))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deletePlant(id: String): Result<Unit> {
        return try {
            plantDao.deletePlantById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun recordWatering(id: String, timestamp: Long): Result<Unit> {
        return try {
            val plant = plantDao.getPlantById(id) ?: return Result.Error(Exception("Plant not found"))
            val nextWatering = Plant.calculateNextWatering(timestamp, plant.wateringFrequencyDays)
            plantDao.updateWateringInfo(id, timestamp, nextWatering)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getFavoritePlants(): Flow<Result<List<Plant>>> {
        return plantDao.getFavoritePlants()
            .map { entities -> entities.map { PlantMapper.mapToDomain(it) } }
            .asResult()
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean): Result<Unit> {
        return try {
            plantDao.updateFavoriteStatus(id, isFavorite)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}