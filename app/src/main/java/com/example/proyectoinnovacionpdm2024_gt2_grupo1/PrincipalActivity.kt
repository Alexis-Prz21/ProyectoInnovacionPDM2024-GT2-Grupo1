package com.example.proyectoinnovacionpdm2024_gt2_grupo1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class PrincipalActivity : AppCompatActivity() {

    private lateinit var emailText: TextView
    private lateinit var cerrarSesion: Button
    private lateinit var agregarFab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val imageList = mutableListOf<ImageItem>()//nuevo
    private lateinit var adapter: ImageAdapter//nuevo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        emailText = findViewById(R.id.tvEmail)
        cerrarSesion = findViewById(R.id.cerrarSesionBtn)
        agregarFab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)

        val bundle = intent.extras
        val email:String? = bundle?.getString("email")
        // Setup
        Setup(email?: "")

        //NUEVO CODIGO CARGAR IMAGENES SUBIDAS AL RECYCLERVIEW======================================

        adapter = ImageAdapter(imageList)//nuevo
        recyclerView.layoutManager = GridLayoutManager(this, 2)//nuevo
        recyclerView.adapter = adapter//nuevo

        loadUserImages()//nuevo

        //==========================================================================================
    }

    private fun Setup(email:String) {
        title = "Inicio"
        emailText.text = email

        cerrarSesion.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            // Cierra sesión de Google
            GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnCompleteListener {
                // Revoca el acceso para que se pueda seleccionar otra cuenta
                GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).revokeAccess().addOnCompleteListener {
                    // Volviendo a la pantalla de autenticación
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        agregarFab.setOnClickListener {
            val intent = Intent(this, SubirImgActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //OBTENER IMAGENES DE LA CARPETA STORAGE DEPENDE DEL CORREO=====================================
    private fun loadUserImages() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$uid/")

            storageRef.listAll().addOnSuccessListener { listResult ->
                for (item in listResult.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        imageList.add(ImageItem(uri.toString()))
                        adapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        // Manejar error al obtener la URL de descarga
                    }
                }
            }.addOnFailureListener {
                // Manejar error al listar las imágenes
            }
        }
    }
    //==============================================================================================
}