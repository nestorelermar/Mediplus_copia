package com.example.mediplus

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recordatorioMedicamentos = findViewById<CardView>(R.id.recordatorioMedicamentos)

        recordatorioMedicamentos.setOnClickListener {
            val modulo_medicamentos = Intent(this, ModuloMedicamentos::class.java)
            startActivity(modulo_medicamentos)
        }

        val recordatorioCitas = findViewById<CardView>(R.id.recordatorioCitas)

        recordatorioCitas.setOnClickListener {
            val modulo_citas = Intent(this, ModuloCitas::class.java)
            startActivity(modulo_citas)
        }

        val recordatorioHidratacion = findViewById<CardView>(R.id.recordatorioHidratacion)

        recordatorioHidratacion.setOnClickListener {
            val modulo_hidratacion = Intent(this, ModuloVidaSaludable::class.java)
            startActivity(modulo_hidratacion)
        }

        val recordatorioAnotaciones = findViewById<CardView>(R.id.recordatorioAnotaciones)

        recordatorioAnotaciones.setOnClickListener {
            startActivity(Intent(this, ModuloGestionSalud::class.java))
        }

        val recordatorioExamenes = findViewById<CardView>(R.id.recordatorioExamenes)

        recordatorioExamenes.setOnClickListener {
                //val modulo_examenes = Intent(this, navigation_drawer::class.java)
                //val modulo_anotaciones = Intent(this, EditarModuloMedicamentos::class.java)
                //startActivity(modulo_examenes)
            //val modulo_examenes = Intent(this, ModuloHistoriales::class.java) este si
            //startActivity(modulo_examenes)
            startActivity(Intent(this, ModuloAlertaAbastecimiento::class.java))
        }


        // Agregar el fragmento a la actividad sin parametros
        /*val fragment = BarTop() // No necesitas pasarle argumentos
        supportFragmentManager.beginTransaction()
            .replace(R.id.myFragmentContainer, fragment)
            .commit()*/


        llamarTopBar()

    }

    fun llamarTopBar() {
        // Obtener el valor de usuarioLogeado desde tu lógica de inicio de sesión
        val usuarioLogeado = intent.getStringExtra("usuarioLogeado") // Asegúrate de que este valor esté disponible

        // Crear una nueva instancia del fragmento
        val fragment = BarTop().apply {
            arguments = Bundle().apply {
                putString("usuarioLogeado", usuarioLogeado) // Pasa el usuario logueado como argumento
            }
        }

        // Agregar el fragmento a la actividad
        supportFragmentManager.beginTransaction()
            .replace(R.id.myFragmentContainer, fragment) // Asegúrate de que este ID coincida con el contenedor en tu layout
            .commit()
    }
}