package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var emailText: TextView
    private lateinit var cerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        emailText = findViewById(R.id.tvEmail)
        cerrarSesion = findViewById(R.id.cerrarSesionBtn)

        val bundle = intent.extras
        val email:String? = bundle?.getString("email")
        // Setup
        Setup(email?: "")
    }

    private fun Setup(email:String) {
        title = "Inicio"
        emailText.text = email

        cerrarSesion.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            // Volviendo a la pantalla anterior
            onBackPressedDispatcher.onBackPressed()
        }
    }
}