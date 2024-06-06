package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var registrarEmail: EditText
    private lateinit var registrarContra: EditText
    private lateinit var registrarUs: Button
    private lateinit var iniciarSesion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        registrarEmail = findViewById(R.id.regitrarEmailET)
        registrarContra = findViewById(R.id.regitrarContraET)
        registrarUs = findViewById(R.id.registrarUsBtn)
        iniciarSesion = findViewById(R.id.tvIniciarSesion)

        // Configurando las acciones del Button y del TextView
        setup()
    }

    private fun setup() {
        title = "Registro de Usuario"

        registrarUs.setOnClickListener(){
            if (registrarEmail.text.isNotEmpty() && registrarContra.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        registrarEmail.text.toString(), registrarContra.text.toString()).addOnCompleteListener{
                            if (it.isSuccessful) {
                                mostrarPrincipal(it.result?.user?.email?: "")
                            } else {
                                val mensaje: String = "El usuario ya se encuentra registrado en el sistema"
                                mostrarAlerta(mensaje)
                            }
                        }
            }
        }

        iniciarSesion.setOnClickListener {
            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarAlerta(mensaje:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialogo: AlertDialog = builder.create()
        dialogo.show()
    }

    private fun mostrarPrincipal(email: String) {
        val principalIntent: Intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(principalIntent)
        finish()
    }
}