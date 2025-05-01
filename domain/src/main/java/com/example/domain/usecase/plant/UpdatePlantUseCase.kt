package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.model.Plant
import com.example.domain.repository.PlantRepository

class UpdatePlantUseCase(private val plantRepository: PlantRepository) : UseCase<Plant, Unit>() {
    override suspend fun execute(params: Plant): Result<Unit> {
        return plantRepository.updatePlant(params)
    }
}