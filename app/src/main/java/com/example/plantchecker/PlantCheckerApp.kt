package com.example.plantchecker

import android.app.Application
import androidx.work.Configuration
import com.example.plantchecker.di.appModule
import com.example.plantchecker.di.useCaseModule
import com.example.plantchecker.di.viewModelModule
import com.example.plantchecker.work.WorkManagerScheduler
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class PlantCheckerApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PlantCheckerApp)
            modules(listOf(
                appModule,
                useCaseModule,
                viewModelModule
            ))
        }

        WorkManagerScheduler.scheduleWateringReminders(applicationContext)
    }


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}