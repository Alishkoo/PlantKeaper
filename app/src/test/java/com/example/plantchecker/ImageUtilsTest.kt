package com.example.plantchecker.util

import org.junit.Assert.assertNull
import org.junit.Test
import java.io.File

class ImageUtilsTest {

    @Test
    fun `loadImageFromPath returns null for non-existent file`() {
        // Arrange
        val nonExistentPath = "/non/existent/path.jpg"

        // Act
        val result = ImageUtils.loadImageFromPath(nonExistentPath)

        // Assert
        assertNull("Should return null for non-existent file", result)
    }

    @Test
    fun `loadImageFromPath returns null for invalid path`() {
        // Arrange - пустой путь
        val emptyPath = ""

        // Act
        val result = ImageUtils.loadImageFromPath(emptyPath)

        // Assert
        assertNull("Should return null for empty path", result)
    }

    @Test
    fun `loadImageFromPath returns null for null path`() {
        // Act
        val result = ImageUtils.loadImageFromPath(null)

        // Assert
        assertNull("Should return null for null path", result)
    }
}