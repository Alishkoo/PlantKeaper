package com.example.plantchecker.di

import com.example.domain.usecase.plant.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetPlantsUseCase(get()) }
    factory { GetPlantByIdUseCase(get()) }
    factory { AddPlantUseCase(get()) }
    factory { UpdatePlantUseCase(get()) }
    factory { DeletePlantUseCase(get()) }
    factory { RecordWateringUseCase(get(), get()) }
    factory { GetFavoritePlantsUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
}