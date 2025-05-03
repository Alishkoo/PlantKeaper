package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.model.CareEvent
import com.example.domain.model.CareType
import com.example.domain.model.Plant
import com.example.domain.repository.CareEventRepository
import com.example.domain.repository.PlantRepository
import java.util.UUID

class RecordWateringUseCase(
    private val plantRepository: PlantRepository,
    private val careEventRepository: CareEventRepository
) : UseCase<RecordWateringUseCase.Params, Unit>() {

    data class Params(val plantId: String, val timestamp: Long = System.currentTimeMillis())

    override suspend fun execute(params: Params): Result<Unit> {

        val wateringResult = plantRepository.recordWatering(params.plantId, params.timestamp)
        if (wateringResult is Result.Error) {
            return wateringResult
        }


        val careEvent = CareEvent(
            id = UUID.randomUUID().toString(),
            plantId = params.plantId,
            careType = CareType.WATERING,
            timestamp = params.timestamp,
            notes = "Plant watered"
        )

        return when (val result = careEventRepository.addCareEvent(careEvent)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> result
            is Result.Loading -> Result.Loading
        }
    }
}