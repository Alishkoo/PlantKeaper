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

    // StateFlow для текущего растения
    private val _plant = MutableStateFlow<Plant>(Plant())
    val plant: StateFlow<Plant> = _plant

    // StateFlow для состояния операции
    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Success(""))
    val uiState: StateFlow<UiState<String>> = _uiState

    // Флаг редактирования/создания
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    // Загрузка существующего растения для редактирования
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

    // Обновление полей растения
    fun updatePlantField(field: PlantField, value: Any) {
        val currentPlant = _plant.value

        val updatedPlant = when (field) {
            PlantField.NAME -> currentPlant.copy(name = value as String)
            PlantField.SPECIES -> currentPlant.copy(species = value as String)
            PlantField.IMAGE_URL -> currentPlant.copy(imageUrl = value as String)
            PlantField.WATERING_FREQUENCY -> currentPlant.copy(wateringFrequencyDays = value as Int)
            PlantField.SUNLIGHT_NEEDS -> currentPlant.copy(sunlightNeeds = value as String)
            PlantField.SOIL_TYPE -> currentPlant.copy(soilType = value as String)
            PlantField.NOTES -> currentPlant.copy(notes = value as String)
        }

        _plant.value = updatedPlant
    }

    // Сохранение растения
    fun savePlant() {
        viewModelScope.launch {
            val currentPlant = _plant.value

            // Валидация
            if (currentPlant.name.isBlank()) {
                _uiState.value = UiState.Error("Plant name cannot be empty")
                return@launch
            }

            _uiState.value = UiState.Loading

            // Если последний полив не указан, устанавливаем текущее время
            val plantToSave = if (currentPlant.lastWateredTimestamp == 0L) {
                currentPlant.copy(lastWateredTimestamp = System.currentTimeMillis())
            } else {
                currentPlant
            }

            // Сохранение нового или обновление существующего растения
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

    // Сброс состояния для нового растения
    fun resetState() {
        _plant.value = Plant(id = UUID.randomUUID().toString())
        _isEditMode.value = false
        _uiState.value = UiState.Success("")
    }
}

// Enum для полей растения
enum class PlantField {
    NAME,
    SPECIES,
    IMAGE_URL,
    WATERING_FREQUENCY,
    SUNLIGHT_NEEDS,
    SOIL_TYPE,
    NOTES
}