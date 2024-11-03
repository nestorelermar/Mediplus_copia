package com.example.mediplus.inicio_sesion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediplus.MainActivity
import com.example.mediplus.R
import com.example.mediplus.Registrarse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.Color
import android.os.Handler

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val buttonIniciarSesion = findViewById<LinearLayout>(R.id.ButtonIniciarSesionLogin)

        buttonIniciarSesion.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            loginUser()
        }

        val buttonRegister = findViewById<LinearLayout>(R.id.ButtonRegister)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

    }

    /*private fun loginUser() {
        val email = findViewById<EditText>(R.id.txtCorreoLogin).text.toString()
        val pass = findViewById<EditText>(R.id.txtPasswordLogin).text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Redirigir a otra actividad
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Opcional: finaliza la actividad actual
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }*/

    /*private fun loginUser() {
        val email = findViewById<EditText>(R.id.txtCorreoLogin).text.toString()
        val pass = findViewById<EditText>(R.id.txtPasswordLogin).text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(userId!!)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val names = document.getString("nombres") ?: ""
                                    val surnames = document.getString("apellidos") ?: ""

                                    // Obtener la primera letra del nombre y apellido en mayúsculas
                                    val firstLetterName = names.firstOrNull()?.toUpperCase() ?: ' '
                                    val firstLetterSurname = surnames.firstOrNull()?.toUpperCase() ?: ' '

                                    // Combinar las letras en una cadena
                                    val usuarioLogeado = "$firstLetterName$firstLetterSurname"

                                    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                    sharedPreferences.edit().putString("usuarioLogeado", usuarioLogeado).apply()

                                    // Redirigir a otra actividad y pasar las iniciales
                                    val intent = Intent(this, MainActivity::class.java).apply {
                                        putExtra("usuarioLogeado", usuarioLogeado) // Pasar las iniciales
                                    }
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al recuperar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.txtCorreoLogin).text.toString()
        val pass = findViewById<EditText>(R.id.txtPasswordLogin).text.toString()
        val loadingIndicator = findViewById<LinearLayout>(R.id.loadingIndicator)
        val loadingDots = findViewById<TextView>(R.id.loadingDots)

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loadingIndicator.visibility = View.VISIBLE // Muestra el loading indicator

            // Iniciar la animación
            animateLoadingDots(loadingDots)

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        // Guardar userId en SharedPreferences
                        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        sharedPreferences.edit().putString("userId", userId).apply()

                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(userId!!)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val names = document.getString("nombres") ?: ""
                                    val surnames = document.getString("apellidos") ?: ""

                                    val firstLetterName = names.firstOrNull()?.uppercaseChar() ?: ' '
                                    val firstLetterSurname = surnames.firstOrNull()?.uppercaseChar() ?: ' '

                                    val usuarioLogeado = "$firstLetterName$firstLetterSurname"

                                    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                    sharedPreferences.edit().putString("usuarioLogeado", usuarioLogeado).apply()
                                    //iniciar(usuarioLogeado)
                                    val intent = Intent(this, MainActivity::class.java).apply {
                                        putExtra("usuarioLogeado", usuarioLogeado)
                                    }
                                    startActivity(intent)
                                    finish()
                                    loadingIndicator.visibility = View.GONE // Oculta el loading indicator
                                } else {
                                    Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al recuperar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateLoadingDots(loadingDots: TextView) {
        val handler = Handler()
        val colors = arrayOf(Color.GRAY, Color.RED, Color.GRAY, Color.GRAY) // Colores de los puntos
        val dotCount = 3
        var currentIndex = 0

        val runnable = object : Runnable {
            override fun run() {
                // Construir la cadena de texto con los colores de los puntos
                val coloredDots = StringBuilder()
                for (i in 0 until dotCount) {
                    if (i == currentIndex) {
                        coloredDots.append("●") // Punto actual en rojo
                    } else {
                        coloredDots.append("●") // Otros puntos en gris
                    }
                }
                loadingDots.text = coloredDots.toString()

                // Cambiar el color del punto actual a rojo
                loadingDots.setTextColor(colors[currentIndex])

                // Incrementar el índice y reiniciar si es necesario
                currentIndex = (currentIndex + 1) % dotCount

                // Repetir el runnable cada 300 ms
                handler.postDelayed(this, 300)
            }
        }

        // Iniciar el bucle de animación
        handler.post(runnable)
    }

    fun iniciar (usuarioLogeado: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("usuarioLogeado", usuarioLogeado)
        }
        startActivity(intent)
        finish()
    }

}