package com.example.proyectoinnovacionpdm2024_gt2_grupo1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var emailText: TextView
    private lateinit var cerrarSesion: Button
    private lateinit var agregarFab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

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


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


        //URL de las imágenes en Firebase Storage
        val imageUrls = listOf(
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Paisaje1.jpg?alt=media&token=f3482efd-fdc9-4695-9c9f-6d97f73b9d1d",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/wlop-61se.jpg?alt=media&token=106dd764-c035-452a-a319-f5d740cc8a65",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Tigre.jpg?alt=media&token=d4cbab0e-5a6b-4e01-99e8-fbf34bafedec",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Paisaje2.jpg?alt=media&token=0062e3c6-939c-41e1-a07b-a7d918c977ea",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/wlop-2see.jpg?alt=media&token=4e9f2b50-ac05-4d03-8825-fe2fc1f9fb77",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Muralla.jpg?alt=media&token=1ad1cb0f-5542-45f9-8ea8-923f943cd9c9",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/ArtStation.jfif?alt=media&token=134054f4-7464-445a-ab18-6c05b0561fc4",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/pinwallpaper.jfif?alt=media&token=3c7df879-9e66-41a4-9edd-6d1906b9473f",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Leones.jpg?alt=media&token=f610844f-5d62-4153-b763-97bc859a18ec",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/Vehiculo.jpg?alt=media&token=7bb67010-4738-4d00-9d1d-2924d2aecdcb",
            "https://firebasestorage.googleapis.com/v0/b/proyectoinnovacion-gt2-g1.appspot.com/o/pinmirror.jfif?alt=media&token=47586a47-15ac-403e-af29-ae432658f574"
        )

        val imageList = imageUrls.map { ImageItem(it) }
        val adapter = ImageAdapter(imageList)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

}
    //-------------------------------------------------------------------------------------------------------

    private fun Setup(email:String) {
        title = "Inicio"
        emailText.text = email

        cerrarSesion.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            // Volviendo a la pantalla anterior
            onBackPressedDispatcher.onBackPressed()
        }

        agregarFab.setOnClickListener {
            val intent = Intent(this, SubirImgActivity::class.java)
            startActivity(intent)
        }
    }


}