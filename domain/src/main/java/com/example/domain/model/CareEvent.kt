package com.example.domain.model


enum class CareType {
    WATERING,
    FERTILIZING,
    REPOTTING,
    PRUNING,
    MISTING,
    OTHER
}


data class CareEvent(
    val id: String = "",
    val plantId: String = "",
    val careType: CareType = CareType.WATERING,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)