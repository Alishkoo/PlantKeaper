package com.example.plantchecker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.*


object ImageUtils {

    fun saveImageFromUri(context: Context, uri: Uri): String? {
        try {

            val imageDir = File(context.filesDir, "plant_images")
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }


            val fileName = "plant_${UUID.randomUUID()}.jpg"
            val file = File(imageDir, fileName)


            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }


            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }



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