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

        fun scheduleWateringReminders(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build()


            val wateringWorkRequest = PeriodicWorkRequestBuilder<WateringReminderWorker>(
                24, TimeUnit.HOURS
            ).setConstraints(constraints)
                .build()


            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WateringReminderWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                wateringWorkRequest
            )
        }
    }
}