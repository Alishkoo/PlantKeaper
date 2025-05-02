package com.example.data.mapper

import com.example.data.local.entity.PlantEntity
import com.example.domain.model.Plant
import org.junit.Assert.assertEquals
import org.junit.Test

class PlantMapperTest {

    @Test
    fun `mapToDomain correctly converts PlantEntity to Plant`() {
        // Arrange
        val plantEntity = PlantEntity(
            id = "123",
            name = "Алоэ",
            species = "Aloe Vera",
            imageUrl = "https://example.com/aloe.jpg",
            wateringFrequencyDays = 7,
            lastWateredTimestamp = 1620000000000,
            nextWateringDue = 1620604800000,
            sunlightNeeds = "Яркий непрямой свет",
            soilType = "Суккулентная смесь",
            notes = "Поставить на южное окно",
            isFavorite = true,
            isSyncedWithServer = false
        )

        // Act
        val plant = PlantMapper.mapToDomain(plantEntity)

        // Assert
        assertEquals("123", plant.id)
        assertEquals("Алоэ", plant.name)
        assertEquals("Aloe Vera", plant.species)
        assertEquals("https://example.com/aloe.jpg", plant.imageUrl)
        assertEquals(7, plant.wateringFrequencyDays)
        assertEquals(1620000000000, plant.lastWateredTimestamp)
        assertEquals(1620604800000, plant.nextWateringDue)
        assertEquals("Яркий непрямой свет", plant.sunlightNeeds)
        assertEquals("Суккулентная смесь", plant.soilType)
        assertEquals("Поставить на южное окно", plant.notes)
        assertEquals(true, plant.isFavorite)
    }

    @Test
    fun `mapToEntity correctly converts Plant to PlantEntity`() {
        // Arrange
        val plant = Plant(
            id = "123",
            name = "Монстера",
            species = "Monstera Deliciosa",
            imageUrl = "https://example.com/monstera.jpg",
            wateringFrequencyDays = 10,
            lastWateredTimestamp = 1620000000000,
            nextWateringDue = 1620864000000,
            sunlightNeeds = "Яркий непрямой свет",
            soilType = "Дренированная почва",
            notes = "Опрыскивать листья",
            isFavorite = false
        )

        // Act
        val entity = PlantMapper.mapToEntity(plant)

        // Assert
        assertEquals("123", entity.id)
        assertEquals("Монстера", entity.name)
        assertEquals("Monstera Deliciosa", entity.species)
        assertEquals("https://example.com/monstera.jpg", entity.imageUrl)
        assertEquals(10, entity.wateringFrequencyDays)
        assertEquals(1620000000000, entity.lastWateredTimestamp)
        assertEquals(1620864000000, entity.nextWateringDue)
        assertEquals("Яркий непрямой свет", entity.sunlightNeeds)
        assertEquals("Дренированная почва", entity.soilType)
        assertEquals("Опрыскивать листья", entity.notes)
        assertEquals(false, entity.isFavorite)
        assertEquals(false, entity.isSyncedWithServer)
    }

    @Test
    fun `mapToEntity with empty id generates new UUID`() {
        // Arrange
        val plant = Plant(
            id = "",  // Пустой ID
            name = "Тестовое растение",
            species = "Test Species",
            imageUrl = "",
            wateringFrequencyDays = 5,
            lastWateredTimestamp = 0,
            nextWateringDue = 0,
            sunlightNeeds = "",
            soilType = "",
            notes = "",
            isFavorite = false
        )

        // Act
        val entity = PlantMapper.mapToEntity(plant)

        // Assert
        assert(entity.id.isNotEmpty()) { "ID должен быть сгенерирован" }
        assertEquals(36, entity.id.length) // UUID имеет длину 36 символов
        assertEquals("Тестовое растение", entity.name)
    }

    @Test
    fun `mapToEntity with syncFlag sets isSyncedWithServer`() {
        // Arrange
        val plant = Plant(
            id = "test-id",
            name = "Тест",
            species = "Test",
            imageUrl = "",
            wateringFrequencyDays = 1,
            lastWateredTimestamp = 0,
            nextWateringDue = 0,
            sunlightNeeds = "",
            soilType = "",
            notes = "",
            isFavorite = false
        )

        // Act
        val entity = PlantMapper.mapToEntity(plant, isSyncedWithServer = true)

        // Assert
        assertEquals(true, entity.isSyncedWithServer)
    }
}