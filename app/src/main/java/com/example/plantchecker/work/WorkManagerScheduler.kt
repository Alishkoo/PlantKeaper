package com.example.plantchecker.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerScheduler {
    companion object {
        // Запуск ежедневной проверки растений, требующих полива
        fun scheduleWateringReminders(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build()

            // Настройка периодической задачи (каждые 24 часа)
            val wateringWorkRequest = PeriodicWorkRequestBuilder<WateringReminderWorker>(
                24, TimeUnit.HOURS
            ).setConstraints(constraints)
                .build()

            // Запланировать задачу с заменой существующей, если она уже есть
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WateringReminderWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                wateringWorkRequest
            )
        }
    }
}