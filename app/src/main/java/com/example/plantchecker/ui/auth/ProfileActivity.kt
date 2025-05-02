package com.example.plantchecker.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ProfileActivity : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)

//        auth = Firebase.auth
//        tvEmail = findViewById(R.id.tv_email)
//        btnLogout = findViewById(R.id.btn_logout)

//        // Проверяем, вошел ли пользователь
//        val user = Firebase.auth.currentUser
//        if (user == null) {
//            // Если пользователь не авторизован, перенаправляем на экран входа
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()  // Закрываем активность профиля
//        } else {
//            // Пользователь авторизован, показываем его email
//            tvEmail.text = user.email
//        }

        // Настраиваем кнопку выхода
//        btnLogout.setOnClickListener {
//            Firebase.auth.signOut()
//            // После выхода перенаправляем на экран входа
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//        }
    }
}