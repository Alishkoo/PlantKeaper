package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.model.Plant
import com.example.domain.repository.PlantRepository

class GetPlantByIdUseCase(private val plantRepository: PlantRepository) : UseCase<String, Plant>() {
    override suspend fun execute(params: String): Result<Plant> {
        return plantRepository.getPlantById(params)
    }
}