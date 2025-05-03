package com.example.plantchecker.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.common.Result as AppResult
import com.example.domain.repository.PlantRepository
import com.example.plantchecker.MainActivity
import com.example.plantchecker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WateringReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val plantRepository: PlantRepository by inject()

    companion object {
        const val WORK_NAME = "WateringReminderWork"
        private const val CHANNEL_ID = "watering_reminders"
        private const val NOTIFICATION_ID = 1
    }

    override suspend fun doWork(): androidx.work.ListenableWorker.Result = withContext(Dispatchers.IO) {
        try {

            val plantsResult = plantRepository.getPlants().first()


            if (plantsResult is AppResult.Success) {
                val plants = plantsResult.data
                val plantsNeedingWater = plants.filter { it.needsWatering }

                if (plantsNeedingWater.isNotEmpty()) {

                    createNotificationChannel()


                    val plantNames = plantsNeedingWater.joinToString(", ") { it.name }
                    val message = if (plantsNeedingWater.size == 1) {
                        "${plantsNeedingWater[0].name} needs watering!"
                    } else {
                        "$plantNames need watering!"
                    }

                    showNotification(message, plantsNeedingWater.size)
                }
            }


            androidx.work.ListenableWorker.Result.success()
        } catch (e: Exception) {
            androidx.work.ListenableWorker.Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Watering Reminders"
            val descriptionText = "Notifications for plants that need watering"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(message: String, plantsCount: Int) {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (plantsCount == 1) "Plant needs watering" else "Plants need watering"

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        with(NotificationManagerCompat.from(applicationContext)) {
            try {
                notify(NOTIFICATION_ID, notification)
            } catch (e: SecurityException) {
                // Обработка ошибки, если нет разрешения
            }
        }
    }
}