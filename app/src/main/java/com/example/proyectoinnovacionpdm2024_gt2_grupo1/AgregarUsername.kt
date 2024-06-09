package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AgregarUsername : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_username)

        val editText = findViewById<EditText>(R.id.EdUsername) // Nuevo
        val saveButton = findViewById<Button>(R.id.saveUser)
        val cancelButton = findViewById<Button>(R.id.cancelUser)

        saveButton.setOnClickListener {
            val username = editText.text.toString()
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("username", username)
                apply()
            }
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

    }
}