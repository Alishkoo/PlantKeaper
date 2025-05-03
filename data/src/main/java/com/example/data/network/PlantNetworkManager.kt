package com.example.plantchecker.ui.network

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.data.network.PlantDataItem
import com.example.data.network.PlantNetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlantNetworkManager(private val fragment: Fragment) {

    private val plantService = PlantNetworkService()
    private val uiScope = CoroutineScope(Dispatchers.Main)


    fun searchPlants(
        query: String,
        onSuccess: (List<PlantDataItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        uiScope.launch {
            try {
                val result = plantService.searchPlants(query)

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        onSuccess(result.data)
                    } else {
                        onError("Не удалось получить данные")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Ошибка: ${e.message}")
                }
            }
        }
    }


    fun getPlantsList(
        page: Int = 1,
        onSuccess: (List<PlantDataItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        uiScope.launch {
            try {
                val result = plantService.getPlants(page)

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        onSuccess(result.data)
                    } else {
                        onError("Не удалось получить данные")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Ошибка: ${e.message}")
                }
            }
        }
    }


    fun loadAndShowFirstPlant() {
        uiScope.launch {
            try {
                val result = plantService.getPlants(1)

                withContext(Dispatchers.Main) {
                    if (result != null && result.data.isNotEmpty()) {
                        val firstPlant = result.data[0]
                        Toast.makeText(
                            fragment.requireContext(),
                            "Получено растение: ${firstPlant.commonName}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            fragment.requireContext(),
                            "Не удалось получить данные о растениях",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Ошибка: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}