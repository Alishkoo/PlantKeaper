package com.example.plantchecker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Утилитный класс для работы с изображениями
 */
object ImageUtils {
    /**
     * Сохраняет изображение из Uri в постоянное хранилище приложения
     * и возвращает путь к сохраненному файлу.
     */
    fun saveImageFromUri(context: Context, uri: Uri): String? {
        try {
            // Создаем директорию для хранения изображений, если она не существует
            val imageDir = File(context.filesDir, "plant_images")
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }

            // Создаем уникальное имя файла
            val fileName = "plant_${UUID.randomUUID()}.jpg"
            val file = File(imageDir, fileName)

            // Копируем изображение из Uri в файл
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            // Возвращаем путь к сохраненному файлу
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Загружает изображение из пути в Bitmap
     */
    fun loadImageFromPath(path: String?): Bitmap? {
        if (path.isNullOrEmpty()) return null

        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}