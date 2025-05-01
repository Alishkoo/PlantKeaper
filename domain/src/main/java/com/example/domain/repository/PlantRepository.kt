package com.example.domain.repository

import com.example.core.common.Result
import com.example.domain.model.Plant
import kotlinx.coroutines.flow.Flow


interface PlantRepository {

    fun getPlants(): Flow<Result<List<Plant>>>


    suspend fun getPlantById(id: String): Result<Plant>
    suspend fun addPlant(plant: Plant): Result<String>
    suspend fun updatePlant(plant: Plant): Result<Unit>
    suspend fun deletePlant(id: String): Result<Unit>
    suspend fun recordWatering(id: String, timestamp: Long = System.currentTimeMillis()): Result<Unit>
    fun getFavoritePlants(): Flow<Result<List<Plant>>>
    suspend fun toggleFavorite(id: String, isFavorite: Boolean): Result<Unit>
}