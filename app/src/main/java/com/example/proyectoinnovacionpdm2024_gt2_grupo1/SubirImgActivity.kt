package com.example.proyectoinnovacionpdm2024_gt2_grupo1

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectoinnovacionpdm2024_gt2_grupo1.databinding.ActivitySubirImgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SubirImgActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubirImgBinding
    lateinit var imageUri: Uri
    lateinit var compressedImageFile: File

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 101
        private const val REQUEST_CODE_SELECT_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubirImgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seleccionarBtn.setOnClickListener {
            if (checkStoragePermission()) {
                seleccionarImagen()
            } else {
                requestStoragePermission()
            }
        }

        binding.subirBtn.setOnClickListener {
            subirImagen()
        }

        binding.regresarHomeBtn.setOnClickListener {
            regresarHome()
        }
    }

    private fun checkStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_STORAGE_PERMISSION
        )
    }

    private fun seleccionarImagen() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    seleccionarImagen()
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun subirImagen() {
        if (!::compressedImageFile.isInitialized) {
            Toast.makeText(this, "Por favor selecciona una imagen primero", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Subiendo archivo...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        //val storageReference = FirebaseStorage.getInstance().getReference("$fileName") LINEA ELIMINADA

        //NUEVO CODIGO==============================================================================

        // Obtener el UID del usuario actual
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Verificar si se obtuvo el UID del usuario
        if (uid.isNullOrEmpty()) {
            progressDialog.dismiss()
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val storageReference = FirebaseStorage.getInstance().getReference("images/$uid/$fileName.jpg")

        //==========================================================================================

        val compressedUri = Uri.fromFile(compressedImageFile)
        storageReference.putFile(compressedUri)
            .addOnSuccessListener {
                binding.ivPrevisualizar.setImageURI(null)
                Toast.makeText(this@SubirImgActivity, "Subido satisfactoriamente", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
            .addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@SubirImgActivity, "Fallado: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarPrincipal(email: String) {
        val principalIntent: Intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(principalIntent)
        finish()
    }

    private fun regresarHome() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        // Enviando el email del usuario activo al home page
        val email = currentUser?.email.toString()
        mostrarPrincipal(email)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.ivPrevisualizar.setImageURI(imageUri)
            comprimirImagen()
        }
    }

    private fun comprimirImagen() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = getFileFromUri(imageUri)
                compressedImageFile = Compressor.compress(this@SubirImgActivity, file) {
                    default(width = 1080, height = 1080, quality = 75)
                }
                withContext(Dispatchers.Main) {
                    binding.ivPrevisualizar.setImageURI(Uri.fromFile(compressedImageFile))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SubirImgActivity, "Error al comprimir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val filePath = cursor.getString(columnIndex)
        cursor.close()
        return File(filePath)
    }
}
