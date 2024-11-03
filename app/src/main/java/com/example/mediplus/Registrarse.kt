package com.example.mediplus

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mediplus.inicio_sesion.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Registrarse : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val ImageButton = findViewById<ImageButton>(R.id.activity_login)

        ImageButton.setOnClickListener {
            finish()
        }

        val buttonRegister = findViewById<LinearLayout>(R.id.ButtonIniciarSesion)

        buttonRegister.setOnClickListener {
            finish()
        }


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val crearCuenta = findViewById<LinearLayout>(R.id.buttonCrearCuenta)

        crearCuenta.setOnClickListener {
            //val crear_cuenta = Intent(this, ConfirmacionUsuarioCreado::class.java)
            //startActivity(crear_cuenta)
            registerUser()
        }

    }

    /*private fun registerUser() {
        val nombres = findViewById<EditText>(R.id.txtNombres).text.toString()
        val apellidos = findViewById<EditText>(R.id.txtApellidos).text.toString()
        val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString()
        val correo = findViewById<EditText>(R.id.txtCorreo).text.toString()
        val contraseña = findViewById<EditText>(R.id.txtPassword).text.toString()

        if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val user = User(nombres, apellidos, telefono, correo)

                        uid?.let {
                            database.reference.child("users").child(uid).setValue(user)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        val crear_cuenta = Intent(this, ConfirmacionUsuarioCreado::class.java)
                                        startActivity(crear_cuenta)
                                    } else {
                                        Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }*/


    /*private fun registerUser() {
        val nombres = findViewById<EditText>(R.id.txtNombres).text.toString()
        val apellidos = findViewById<EditText>(R.id.txtApellidos).text.toString()
        val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString()
        val correo = findViewById<EditText>(R.id.txtCorreo).text.toString()
        val contraseña = findViewById<EditText>(R.id.txtPassword).text.toString()

        if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
            val registerButton = findViewById<LinearLayout>(R.id.buttonCrearCuenta)
            registerButton.isEnabled = false // Deshabilitar el botón

            // Crear un ProgressDialog
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Cargando...")
            progressDialog.setCancelable(false) // Evitar que se cierre al tocar fuera
            progressDialog.show() // Mostrar el ProgressDialog

            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener { task ->
                    progressDialog.dismiss() // Ocultar el ProgressDialog
                    registerButton.isEnabled = true // Habilitar el botón

                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val user = hashMapOf(
                            "nombres" to nombres,
                            "apellidos" to apellidos,
                            "telefono" to telefono,
                            "correo" to correo
                        )

                        // Instancia de Firestore
                        val db = FirebaseFirestore.getInstance()

                        uid?.let {
                            // Guardar el usuario en la colección "users" en Firestore
                            db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    // Redirigir a la actividad de confirmación
                                    val crear_cuenta = Intent(this, ConfirmacionUsuarioCreado::class.java)
                                    startActivity(crear_cuenta)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun registerUser() {
        val db = FirebaseFirestore.getInstance()

        // Obtener los datos de los EditText
        val nombres = findViewById<EditText>(R.id.txtNombres).text.toString()
        val apellidos = findViewById<EditText>(R.id.txtApellidos).text.toString()
        val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString()
        val correo = findViewById<EditText>(R.id.txtCorreo).text.toString()
        val contraseña = findViewById<EditText>(R.id.txtPassword).text.toString()

        // Validar que los campos no estén vacíos
        if (correo.isNotEmpty() && contraseña.isNotEmpty() && nombres.isNotEmpty() && apellidos.isNotEmpty()) {
            val registerButton = findViewById<LinearLayout>(R.id.buttonCrearCuenta)
            registerButton.isEnabled = false // Deshabilitar el botón

            // Registrar el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener { task ->
                    registerButton.isEnabled = true // Habilitar el botón

                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val user = hashMapOf(
                            "nombres" to nombres,
                            "apellidos" to apellidos,
                            "telefono" to telefono,
                            "correo" to correo
                        )

                        uid?.let {
                            // Guardar el usuario en la colección "users" en Firestore
                            db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    // Mensaje de éxito
                                    //Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()

                                    redirectToConfirmation()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para redirigir a la actividad de confirmación
    private fun redirectToConfirmation() {
        val confirm = Intent(this, ConfirmacionUsuarioCreado::class.java)
        startActivity(confirm)
        finish() // Opcional: Cierra la actividad actual si no quieres volver a ella
    }

}