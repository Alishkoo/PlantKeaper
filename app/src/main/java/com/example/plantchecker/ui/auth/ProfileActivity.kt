package com.example.plantchecker.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasev1.launcher.FirebaseAuthKit


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Launch the FirebaseAuthKit Login screen
        FirebaseAuthKit.start(this)
    }
}