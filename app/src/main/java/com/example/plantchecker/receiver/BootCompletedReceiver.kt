package com.example.plantchecker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.plantchecker.work.WorkManagerScheduler

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            WorkManagerScheduler.scheduleWateringReminders(context)
        }
    }
}