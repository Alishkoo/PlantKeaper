package com.example.plantchecker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.plantchecker.ui.auth.ProfileActivity
import com.example.plantchecker.ui.compose.ComposeCalendarActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настраиваем навигацию
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Настраиваем нижнюю навигацию
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Добавляем обработчик для кастомных и стандартных пунктов
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_calendar -> {
                    // Запускаем активность календаря
                    startActivity(Intent(this, ComposeCalendarActivity::class.java))
                    true // Возвращаем true для завершения обработки
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true // Возвращаем true для завершения обработки
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController) || false
                }
            }
        }
    }
}