package com.example.plantchecker.di

import com.example.plantchecker.ui.addplant.AddPlantViewModel
import com.example.plantchecker.ui.home.HomeViewModel
import com.example.plantchecker.ui.plantdetail.PlantDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { (plantId: String?) -> PlantDetailViewModel(plantId, get(), get(), get(), get()) }
    viewModel { AddPlantViewModel(get(), get(), get()) }
}