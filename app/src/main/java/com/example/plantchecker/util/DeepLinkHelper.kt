package com.example.plantchecker.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Вспомогательный класс для тестирования deep links
 */
object DeepLinkHelper {

    /**
     * Создает intent для открытия домашнего экрана
     */
    fun createHomeDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://home"))
    }

    /**
     * Создает intent для добавления нового растения
     */
    fun createAddPlantDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://add"))
    }

    /**
     * Создает intent для добавления нового растения с предзаполненным видом
     */
    fun createAddPlantWithSpeciesDeepLink(species: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://add/${species}"))
    }

    /**
     * Создает intent для просмотра деталей растения по ID
     */
    fun createPlantDetailDeepLink(plantId: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://plant/${plantId}"))
    }

    /**
     * Создает intent для открытия календаря
     */
    fun createCalendarDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://calendar"))
    }

    /**
     * Открывает deep link внутри приложения (для тестирования)
     */
    fun openDeepLink(context: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }
}