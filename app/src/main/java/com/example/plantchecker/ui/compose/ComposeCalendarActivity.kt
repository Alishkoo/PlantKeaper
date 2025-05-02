package com.example.plantchecker.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.plantchecker.ui.theme.PlantCheckerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ComposeCalendarActivity : ComponentActivity() {

    private val viewModel: CalendarViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем, пришла ли дата из deeplink
        val selectedDateStr = intent.getStringExtra("selected_date")
        if (selectedDateStr != null) {
            try {
                // Пытаемся преобразовать строку в LocalDate
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val selectedDate = LocalDate.parse(selectedDateStr, formatter)
                viewModel.selectDate(selectedDate)

                // Также обновляем текущий месяц, чтобы показать правильный месяц
                val yearMonth = selectedDate.let { java.time.YearMonth.of(it.year, it.month) }
                viewModel.setCurrentMonth(yearMonth)
            } catch (e: Exception) {
                // Если формат даты неправильный, игнорируем
            }
        }

        setContent {
            PlantCheckerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarScreen(
                        viewModel = viewModel,
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}