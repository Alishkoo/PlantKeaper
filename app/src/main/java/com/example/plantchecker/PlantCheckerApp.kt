package com.example.plantchecker

import android.app.Application
import com.example.plantchecker.di.appModule
import com.example.plantchecker.di.useCaseModule
import com.example.plantchecker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PlantCheckerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Инициализация Koin
        startKoin {
            // Логирование для отладки
            androidLogger(Level.ERROR)
            // Передаем контекст приложения
            androidContext(this@PlantCheckerApp)
            // Загружаем модули Koin
            modules(listOf(
                appModule,
                useCaseModule,
                viewModelModule
            ))
        }
    }
}