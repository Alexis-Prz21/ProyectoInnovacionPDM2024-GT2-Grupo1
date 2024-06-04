package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var registrar: Button
    private lateinit var acceder: Button
    private lateinit var email: EditText
    private lateinit var contra: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        registrar = findViewById(R.id.registrarBtn)
        acceder = findViewById(R.id.accederBtn)
        email = findViewById(R.id.emailtET)
        contra = findViewById(R.id.contraET)

        // Creación del método Setup
        setup()
    }

    private fun setup() {
        title = "Autenticación"

        registrar.setOnClickListener(){
            if (email.text.isNotEmpty() && contra.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                    email.text.toString(), contra.text.toString()).addOnCompleteListener{

                    if (it.isSuccessful) {
                        mostrarPrincipal(it.result?.user?.email?: "")
                    } else {
                        mostrarAlerta()
                    }
                }
            }
        }

        acceder.setOnClickListener(){
            if (email.text.isNotEmpty() && contra.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        email.text.toString(), contra.text.toString()).addOnCompleteListener{

                        if (it.isSuccessful) {
                            mostrarPrincipal(it.result?.user?.email?: "")
                        } else {
                            mostrarAlerta()
                        }
                    }
            }
        }
    }

    private fun mostrarAlerta() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialogo: AlertDialog = builder.create()
        dialogo.show()
    }

    private fun mostrarPrincipal(email: String) {
        val principalIntent: Intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(principalIntent)
    }
}