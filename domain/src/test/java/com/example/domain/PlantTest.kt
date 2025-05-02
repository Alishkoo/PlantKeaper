package com.example.domain.model

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class PlantTest {

    @Test
    fun `calculateNextWatering correctly calculates next watering date`() {
        val lastWatered = 1625000000000L
        val frequencyDays = 7
        val expectedNextWatering = lastWatered + TimeUnit.DAYS.toMillis(frequencyDays.toLong())


        val result = Plant.calculateNextWatering(lastWatered, frequencyDays)


        assertEquals(expectedNextWatering, result)
    }

    @Test
    fun `needsWatering returns true when current time is past next watering`() {
        // Arrange
        val currentTime = System.currentTimeMillis()
        val pastTime = currentTime - 100000

        val plant = Plant(
            id = "test",
            name = "Test Plant",
            species = "Test Species",
            wateringFrequencyDays = 7,
            lastWateredTimestamp = currentTime - TimeUnit.DAYS.toMillis(8),
            nextWateringDue = pastTime
        )

        assertTrue(plant.needsWatering)
    }

    @Test
    fun `needsWatering returns false when current time is before next watering`() {
        val currentTime = System.currentTimeMillis()
        val futureTime = currentTime + TimeUnit.DAYS.toMillis(2)

        val plant = Plant(
            id = "test",
            name = "Test Plant",
            species = "Test Species",
            wateringFrequencyDays = 7,
            lastWateredTimestamp = currentTime,
            nextWateringDue = futureTime
        )

        assertFalse(plant.needsWatering)
    }

    @Test
    fun `default values are initialized correctly`() {
        val plant = Plant()

        assertEquals("", plant.id)
        assertEquals("", plant.name)
        assertEquals("", plant.species)
        assertEquals("", plant.imageUrl)
        assertEquals(7, plant.wateringFrequencyDays)
        assertEquals(0L, plant.lastWateredTimestamp)
        assertEquals(0L, plant.nextWateringDue)
        assertEquals("", plant.sunlightNeeds)
        assertEquals("", plant.soilType)
        assertEquals("", plant.notes)
        assertFalse(plant.isFavorite)
    }
}