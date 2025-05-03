package com.example.plantchecker.ui.plantdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.model.CareEvent
import com.example.domain.model.Plant
import com.example.domain.usecase.plant.GetPlantByIdUseCase
import com.example.domain.usecase.plant.RecordWateringUseCase
import com.example.domain.usecase.plant.ToggleFavoriteUseCase
import com.example.domain.usecase.plant.DeletePlantUseCase
import com.example.plantchecker.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlantDetailViewModel(
    private val plantId: String?,
    private val getPlantByIdUseCase: GetPlantByIdUseCase,
    private val recordWateringUseCase: RecordWateringUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deletePlantUseCase: DeletePlantUseCase
) : ViewModel() {


    private val _plantState = MutableStateFlow<UiState<Plant>>(UiState.Loading)
    val plantState: StateFlow<UiState<Plant>> = _plantState


    private val _careEvents = MutableStateFlow<UiState<List<CareEvent>>>(UiState.Loading)
    val careEvents: StateFlow<UiState<List<CareEvent>>> = _careEvents


    private val _operationState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val operationState: StateFlow<UiState<Unit>> = _operationState

    init {
       
        plantId?.let { loadPlant(it) }
    }

   
    fun loadPlant(id: String) {
        viewModelScope.launch {
            _plantState.value = UiState.Loading
            when (val result = getPlantByIdUseCase(id)) {
                is Result.Success -> _plantState.value = UiState.Success(result.data)
                is Result.Error -> _plantState.value = UiState.Error(
                    result.exception.message ?: "Failed to load plant"
                )
                is Result.Loading -> _plantState.value = UiState.Loading
            }
        }
    }


    fun waterPlant() {
        viewModelScope.launch {
            plantId?.let { id ->
                _operationState.value = UiState.Loading
                val params = RecordWateringUseCase.Params(id)

                when (val result = recordWateringUseCase(params)) {
                    is Result.Success -> {
                        _operationState.value = UiState.Success(Unit)
                        loadPlant(id)
                    }
                    is Result.Error -> _operationState.value = UiState.Error(
                        result.exception.message ?: "Failed to record watering"
                    )
                    is Result.Loading -> _operationState.value = UiState.Loading
                }
            }
        }
    }


    fun toggleFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            plantId?.let { id ->
                val params = ToggleFavoriteUseCase.Params(id, isFavorite)

                when (val result = toggleFavoriteUseCase(params)) {
                    is Result.Success -> loadPlant(id)
                    is Result.Error -> _operationState.value = UiState.Error(
                        result.exception.message ?: "Failed to update favorite status"
                    )
                    is Result.Loading -> _operationState.value = UiState.Loading
                }
            }
        }
    }


    fun deletePlant() {
        viewModelScope.launch {
            plantId?.let { id ->
                _operationState.value = UiState.Loading

                when (val result = deletePlantUseCase(id)) {
                    is Result.Success -> _operationState.value = UiState.Success(Unit)
                    is Result.Error -> _operationState.value = UiState.Error(
                        result.exception.message ?: "Failed to delete plant"
                    )
                    is Result.Loading -> _operationState.value = UiState.Loading
                }
            }
        }
    }
}