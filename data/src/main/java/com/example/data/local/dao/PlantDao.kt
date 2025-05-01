package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants ORDER BY name ASC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getPlantById(id: String): PlantEntity?

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlantByIdFlow(id: String): Flow<PlantEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)

    @Query("DELETE FROM plants WHERE id = :id")
    suspend fun deletePlantById(id: String)

    @Query("UPDATE plants SET lastWateredTimestamp = :timestamp, nextWateringDue = :nextWateringDue WHERE id = :id")
    suspend fun updateWateringInfo(id: String, timestamp: Long, nextWateringDue: Long)

    @Query("SELECT * FROM plants WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePlants(): Flow<List<PlantEntity>>

    @Query("UPDATE plants SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
}