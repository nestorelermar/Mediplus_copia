package com.example.mediplus

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ModuloHistoriales : AppCompatActivity() {

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo_historiales)

        // Recuperar el userId de SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null) ?: ""

        /* Codigo para traer las iniciales del usuario logueado*/
        val usuarioLogeado = sharedPreferences.getString("usuarioLogeado", "")

        // Crear una nueva instancia del fragmento
        val fragment = BarTop().apply {
            arguments = Bundle().apply {
                putString("usuarioLogeado", usuarioLogeado) // Pasa el usuario logueado como argumento
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.myFragmentContainer, fragment)
            .commit()
        /**/

        /**/
        findViewById<MaterialCardView>(R.id.button_historial_medicamentos).setOnClickListener {
            startActivity(Intent(this, HistorialModuloMedicamentos::class.java))
        }

        findViewById<MaterialCardView>(R.id.button_historial_abastecimiento).setOnClickListener {
            startActivity(Intent(this, CrearModuloVidaSaludable::class.java))
        }

        findViewById<MaterialCardView>(R.id.button_historial_citas_medicas).setOnClickListener {
            startActivity(Intent(this, CrearModuloVidaSaludable::class.java))
        }

        findViewById<MaterialCardView>(R.id.button_historial_gestion_salud).setOnClickListener {
            startActivity(Intent(this, CrearModuloVidaSaludable::class.java))
        }

        findViewById<MaterialCardView>(R.id.button_historial_vida_saludable).setOnClickListener {
            startActivity(Intent(this, HistorialVidaSaludable::class.java))
        }
        /**/

    }
}