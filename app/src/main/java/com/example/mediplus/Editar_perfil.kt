package com.example.mediplus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Editar_perfil : AppCompatActivity() {

    private lateinit var userId: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val buttonRegressMenuPrincipal = findViewById<LinearLayout>(R.id.buttom_regresar_perfil)
        buttonRegressMenuPrincipal.setOnClickListener {
            finish()
        }

        // Recuperar los datos del Intent
        val nombres = intent.getStringExtra("nombres")
        val apellidos = intent.getStringExtra("apellidos")
        val telefono = intent.getStringExtra("telefono")
        val correo = intent.getStringExtra("correo")

        // Asignar los datos a los EditText o TextView en el layout de editar perfil
        findViewById<EditText>(R.id.editTxtNombres).setText(nombres)
        findViewById<EditText>(R.id.editTxtApellidos).setText(apellidos)
        findViewById<EditText>(R.id.editTxtTelefono).setText(telefono)
        findViewById<EditText>(R.id.editTxtCorreo).setText(correo)

        // Recuperar el userId de SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null) ?: ""

        // Configurar el botón de guardar
        val btnGuardar = findViewById<LinearLayout>(R.id.guardarPerfil)
        btnGuardar.setOnClickListener {
            updateUserData()
        }
    }

    private fun updateUserData() {
        val db = FirebaseFirestore.getInstance()

        // Obtener los nuevos datos de los EditText
        val nombresActualizados = findViewById<EditText>(R.id.editTxtNombres).text.toString()
        val apellidosActualizados = findViewById<EditText>(R.id.editTxtApellidos).text.toString()
        val telefonoActualizado = findViewById<EditText>(R.id.editTxtTelefono).text.toString()
        val correoActualizado = findViewById<EditText>(R.id.editTxtCorreo).text.toString()
        val passwordActualizado = findViewById<EditText>(R.id.editTxtPassword).text.toString()

        // Asegúrate de que userId no sea nulo o vacío
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa con los datos a actualizar en Firestore
        val usuarioActualizado = hashMapOf(
            "nombres" to nombresActualizados,
            "apellidos" to apellidosActualizados,
            "telefono" to telefonoActualizado,
            "correo" to correoActualizado
        )

        // Actualizar el documento del usuario en Firestore
        db.collection("users").document(userId)
            .set(usuarioActualizado, SetOptions.merge())
            .addOnSuccessListener {
                // Actualizar el correo en Firebase Authentication
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    // Actualizar el correo
                    user.updateEmail(correoActualizado)
                        .addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                // Si se proporciona una nueva contraseña, actualizarla
                                if (passwordActualizado.isNotEmpty()) {
                                    user.updatePassword(passwordActualizado)
                                        .addOnCompleteListener { passwordTask ->
                                            if (passwordTask.isSuccessful) {
                                                //Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                                                //finish() // Cerrar la actividad después de la actualización

                                                // Redirigir a la actividad de confirmación
                                                val confirmar = Intent(this, ConfirmacionUsuarioActualizado::class.java)
                                                startActivity(confirmar)
                                                finish()
                                            } else {
                                                Toast.makeText(this, "Error al actualizar la contraseña: ${passwordTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    //Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                                    //finish() // Cerrar la actividad si no se actualiza la contraseña
                                    // Redirigir a la actividad de confirmación
                                    val confirmar = Intent(this, ConfirmacionUsuarioActualizado::class.java)
                                    startActivity(confirmar)
                                    finish()
                                }
                            } else {
                                Toast.makeText(this, "Error al actualizar el correo: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar datos en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




}