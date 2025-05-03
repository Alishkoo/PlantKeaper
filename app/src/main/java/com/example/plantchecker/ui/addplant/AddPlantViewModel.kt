package com.example.plantchecker.ui.addplant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Result
import com.example.domain.model.Plant
import com.example.domain.usecase.plant.AddPlantUseCase
import com.example.domain.usecase.plant.GetPlantByIdUseCase
import com.example.domain.usecase.plant.UpdatePlantUseCase
import com.example.plantchecker.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddPlantViewModel(
    private val addPlantUseCase: AddPlantUseCase,
    private val updatePlantUseCase: UpdatePlantUseCase,
    private val getPlantByIdUseCase: GetPlantByIdUseCase
) : ViewModel() {


    private val _plant = MutableStateFlow<Plant>(Plant())
    val plant: StateFlow<Plant> = _plant


    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Success(""))
    val uiState: StateFlow<UiState<String>> = _uiState


    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode


    fun loadPlant(plantId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = getPlantByIdUseCase(plantId)) {
                is Result.Success -> {
                    _plant.value = result.data
                    _isEditMode.value = true
                    _uiState.value = UiState.Success("")
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(
                        result.exception.message ?: "Failed to load plant"
                    )
                }
                is Result.Loading -> _uiState.value = UiState.Loading
            }
        }
    }



    fun updatePlantField(field: PlantField, value: Any) {
        val currentPlant = _plant.value
        val updatedPlant = when (field) {
            is PlantField.NAME -> currentPlant.copy(name = value as String)
            is PlantField.SPECIES -> currentPlant.copy(species = value as String)
            is PlantField.WATERING_FREQUENCY -> {
                val frequency = value as Int
                val nextWateringDue = System.currentTimeMillis() + (frequency * 24 * 60 * 60 * 1000L)
                currentPlant.copy(
                    wateringFrequencyDays = frequency,
                    nextWateringDue = nextWateringDue
                )
            }
            is PlantField.SUNLIGHT_NEEDS -> currentPlant.copy(sunlightNeeds = value as String)
            is PlantField.SOIL_TYPE -> currentPlant.copy(soilType = value as String)
            is PlantField.NOTES -> currentPlant.copy(notes = value as String)
            is PlantField.IMAGE_URL -> currentPlant.copy(imageUrl = value as String)
        }
        _plant.value = updatedPlant
    }


    fun savePlant() {
        viewModelScope.launch {
            val currentPlant = _plant.value


            if (currentPlant.name.isBlank()) {
                _uiState.value = UiState.Error("Plant name cannot be empty")
                return@launch
            }

            _uiState.value = UiState.Loading


            val plantToSave = if (currentPlant.lastWateredTimestamp == 0L) {
                currentPlant.copy(lastWateredTimestamp = System.currentTimeMillis())
            } else {
                currentPlant
            }


            val result = if (_isEditMode.value) {
                updatePlantUseCase(plantToSave)
            } else {
                addPlantUseCase(plantToSave)
            }

            when (result) {
                is Result.Success -> {
                    val successMessage = if (_isEditMode.value) "Plant updated" else "Plant added"
                    _uiState.value = UiState.Success(successMessage)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(
                        result.exception.message ?: "Failed to save plant"
                    )
                }
                is Result.Loading -> _uiState.value = UiState.Loading
            }
        }
    }


    fun resetState() {
        _plant.value = Plant(id = UUID.randomUUID().toString())
        _isEditMode.value = false
        _uiState.value = UiState.Success("")
    }
}


sealed class PlantField {
    object NAME : PlantField()
    object SPECIES : PlantField()
    object WATERING_FREQUENCY : PlantField()
    object SUNLIGHT_NEEDS : PlantField()
    object SOIL_TYPE : PlantField()
    object NOTES : PlantField()
    object IMAGE_URL : PlantField()
}