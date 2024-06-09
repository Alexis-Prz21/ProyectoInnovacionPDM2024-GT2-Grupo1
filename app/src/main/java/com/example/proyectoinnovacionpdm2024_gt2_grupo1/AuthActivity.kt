package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {

    private lateinit var registrar: TextView
    private lateinit var acceder: Button
    private lateinit var email: EditText
    private lateinit var contra: EditText
    private lateinit var googleIniSesion: Button
    private lateinit var googleSignClient: GoogleSignInClient

    companion object {
        private const val SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        comprobarSesion()

        registrar = findViewById(R.id.tvRegistrar)
        acceder = findViewById(R.id.accederBtn)
        email = findViewById(R.id.emailtET)
        contra = findViewById(R.id.contraET)
        googleIniSesion = findViewById(R.id.iniciarGoogleBtn)

        // Configurando el Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignClient = GoogleSignIn.getClient(this,gso)

        // Creación del método Setup
        setup()
    }

    private fun setup() {
        title = "Login"

        acceder.setOnClickListener(){
            if (email.text.isNotEmpty() && contra.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        email.text.toString(), contra.text.toString()).addOnCompleteListener{

                        if (it.isSuccessful) {
                            mostrarPrincipal(it.result?.user?.email?: "")
                        } else {
                            val mensaje: String = "El usuario no se encuentra registrado en el sistema"
                            mostrarAlerta(mensaje)
                        }
                    }
            }
        }

        registrar.setOnClickListener {
            val intent = Intent(this,RegistroActivity::class.java)
            startActivity(intent)
            finish()
        }

        googleIniSesion.setOnClickListener {
            iniciarConGoogle()
        }
    }

    private fun iniciarConGoogle() {
        val signInIntent = googleSignClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseInicioGoogle(account.idToken!!)
            }
            catch (e: ApiException) {
                Log.w("AuthActivity", "Inicio de sesión con Google fallado",e)
            }
        }
    }

    private fun firebaseInicioGoogle(idToken: String) {
        val credencial = GoogleAuthProvider.getCredential(idToken,null)
        FirebaseAuth.getInstance().signInWithCredential(credencial)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    mostrarPrincipal(user?.email?:"")
                }
                else {
                    mostrarAlerta("Fallo en la autenticación con Google")
                }
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

    private fun comprobarSesion() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser?.email.toString()
            mostrarPrincipal(email)
        }
    }
}