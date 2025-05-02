package com.example.plantchecker.ui.compose.model

import java.time.LocalDate

data class WateringEvent(
    val plantId: String,
    val plantName: String,
    val plantSpecies: String,
    val date: LocalDate
)