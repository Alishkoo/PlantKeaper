package com.example.plantchecker.util

import android.content.Context
import android.content.Intent
import android.net.Uri


object DeepLinkHelper {


    fun createHomeDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://home"))
    }


    fun createAddPlantDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://add"))
    }


    fun createAddPlantWithSpeciesDeepLink(species: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://add/${species}"))
    }


    fun createPlantDetailDeepLink(plantId: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://plant/${plantId}"))
    }

    fun createCalendarDeepLink(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("plantchecker://calendar"))
    }


    fun openDeepLink(context: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }
}