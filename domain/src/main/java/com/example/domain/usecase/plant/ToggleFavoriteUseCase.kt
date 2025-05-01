package com.example.domain.usecase.plant

import com.example.core.common.Result
import com.example.core.usecase.UseCase
import com.example.domain.repository.PlantRepository

class ToggleFavoriteUseCase(private val plantRepository: PlantRepository) : UseCase<ToggleFavoriteUseCase.Params, Unit>() {

    data class Params(val plantId: String, val isFavorite: Boolean)

    override suspend fun execute(params: Params): Result<Unit> {
        return plantRepository.toggleFavorite(params.plantId, params.isFavorite)
    }
}