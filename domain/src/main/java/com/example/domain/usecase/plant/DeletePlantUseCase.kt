package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.repository.PlantRepository

class DeletePlantUseCase(private val plantRepository: PlantRepository) : UseCase<String, Unit>() {
    override suspend fun execute(params: String): Result<Unit> {
        return plantRepository.deletePlant(params)
    }
}