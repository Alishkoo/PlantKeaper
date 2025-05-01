package com.example.plantchecker.di

import com.example.data.local.PlantDatabase
import com.example.data.repository.CareEventRepositoryImpl
import com.example.data.repository.PlantRepositoryImpl
import com.example.data.repository.ReminderRepositoryImpl
import com.example.domain.repository.CareEventRepository
import com.example.domain.repository.PlantRepository
import com.example.domain.repository.ReminderRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // Single instance of Room database
    single { PlantDatabase.getInstance(androidContext()) }

    // DAOs
    single { get<PlantDatabase>().plantDao() }
    single { get<PlantDatabase>().careEventDao() }
    single { get<PlantDatabase>().reminderDao() }

    // Repositories
    single<PlantRepository> { PlantRepositoryImpl(get()) }
    single<CareEventRepository> { CareEventRepositoryImpl(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }
}