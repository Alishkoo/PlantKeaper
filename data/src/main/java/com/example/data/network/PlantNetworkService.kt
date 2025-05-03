package com.example.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlantNetworkService {

    private val networkClient = NetworkClient()
    private val gson = Gson()

    companion object {
        private const val TAG = "PlantNetworkService"
        private const val BASE_URL = "https://perenual.com/api"
        private const val API_KEY = "sk-pMvC645d32b5ed3582096"
    }


    suspend fun getPlants(page: Int = 1): PlantListResponse? = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/species-list?key=$API_KEY&page=$page"
            val response = networkClient.get(url)

            if (response != null) {
                return@withContext gson.fromJson(response, PlantListResponse::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении списка растений", e)
        }

        return@withContext null
    }


    suspend fun getPlantById(id: Int): PlantDetailResponse? = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/species/details/$id?key=$API_KEY"
            val response = networkClient.get(url)

            if (response != null) {
                return@withContext gson.fromJson(response, PlantDetailResponse::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении информации о растении", e)
        }

        return@withContext null
    }


    suspend fun searchPlants(query: String): PlantListResponse? = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/species-list?key=$API_KEY&q=$query"
            val response = networkClient.get(url)

            if (response != null) {
                return@withContext gson.fromJson(response, PlantListResponse::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при поиске растений", e)
        }

        return@withContext null
    }
}

data class PlantListResponse(
    @SerializedName("data")
    val data: List<PlantDataItem>,

    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("total")
    val total: Int
)


data class PlantDataItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: String,

    @SerializedName("cycle")
    val cycle: String?,

    @SerializedName("watering")
    val watering: String?,

    @SerializedName("default_image")
    val defaultImage: PlantImage?
)


data class PlantDetailResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: String,

    @SerializedName("cycle")
    val cycle: String?,

    @SerializedName("watering")
    val watering: String?,

    @SerializedName("sunlight")
    val sunlight: List<String>?,

    @SerializedName("default_image")
    val defaultImage: PlantImage?,

    @SerializedName("description")
    val description: String?
)


data class PlantImage(
    @SerializedName("regular_url")
    val regularUrl: String?,

    @SerializedName("small_url")
    val smallUrl: String?,

    @SerializedName("thumbnail")
    val thumbnail: String?
)