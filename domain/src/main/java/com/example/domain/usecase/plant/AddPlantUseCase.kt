package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.model.Plant
import com.example.domain.repository.PlantRepository

class AddPlantUseCase(private val plantRepository: PlantRepository) : UseCase<Plant, String>() {
    override suspend fun execute(params: Plant): Result<String> {
        val plantWithNextWatering = if (params.lastWateredTimestamp > 0 && params.nextWateringDue == 0L) {
            // Если задана дата последнего полива, но не задана дата следующего - вычисляем ее
            params.copy(
                nextWateringDue = Plant.calculateNextWatering(
                    params.lastWateredTimestamp,
                    params.wateringFrequencyDays
                )
            )
        } else {
            params
        }

        return plantRepository.addPlant(plantWithNextWatering)
    }
}