package com.example.domain.model


data class Reminder(
    val id: String = "",
    val plantId: String = "",
    val title: String = "",
    val message: String = "",
    val triggerTime: Long = 0,
    val isRecurring: Boolean = false,
    val recurrenceIntervalDays: Int = 0
)