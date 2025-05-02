package com.example.plantchecker.util

import android.content.Intent
import android.net.Uri
import org.junit.Assert.*
import org.junit.Test

class DeepLinkHelperTest {

    @Test
    fun `createHomeDeepLink should create correct Intent`() {
        // Act
        val intent = DeepLinkHelper.createHomeDeepLink()

        // Assert
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Uri.parse("plantchecker://home"), intent.data)
    }

    @Test
    fun `createAddPlantDeepLink should create correct Intent`() {
        // Act
        val intent = DeepLinkHelper.createAddPlantDeepLink()

        // Assert
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Uri.parse("plantchecker://add"), intent.data)
    }

    @Test
    fun `createAddPlantWithSpeciesDeepLink should encode species name properly`() {
        // Arrange
        val species = "Aloe Vera"

        // Act
        val intent = DeepLinkHelper.createAddPlantWithSpeciesDeepLink(species)

        // Assert
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Uri.parse("plantchecker://add/Aloe%20Vera"), intent.data)
    }

    @Test
    fun `createPlantDetailDeepLink should include plantId`() {
        // Arrange
        val plantId = "test-123"

        // Act
        val intent = DeepLinkHelper.createPlantDetailDeepLink(plantId)

        // Assert
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Uri.parse("plantchecker://plant/test-123"), intent.data)
    }

    @Test
    fun `createCalendarDeepLink should create correct Intent`() {
        // Act
        val intent = DeepLinkHelper.createCalendarDeepLink()

        // Assert
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Uri.parse("plantchecker://calendar"), intent.data)
    }
}