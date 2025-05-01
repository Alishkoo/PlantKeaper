package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.NoParamUseCase
import com.example.domain.model.Plant
import com.example.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetPlantsUseCase(private val plantRepository: PlantRepository) : NoParamUseCase<List<Plant>>() {
    override suspend fun execute(): Result<List<Plant>> {
        return plantRepository.getPlants().first()
    }

    fun getFlow(): Flow<Result<List<Plant>>> {
        return plantRepository.getPlants()
    }
}