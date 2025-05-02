package com.example.domain.model



data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val imageUrl: String = "",
    val wateringFrequencyDays: Int = 7,
    val lastWateredTimestamp: Long = 0,
    val nextWateringDue: Long = 0,
    val sunlightNeeds: String = "",
    val soilType: String = "",
    val notes: String = "",
    val isFavorite: Boolean = false
) {

    val needsWatering: Boolean
        get() = System.currentTimeMillis() >= nextWateringDue


    companion object {
        fun calculateNextWatering(lastWatered: Long, frequencyDays: Int): Long {
            return lastWatered + (frequencyDays * 24 * 60 * 60 * 1000L)
        }
    }
}