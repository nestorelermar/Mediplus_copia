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

class ModuloCitas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo_citas)

        // Codigo de TopBar
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val usuarioLogeado = sharedPreferences.getString("usuarioLogeado", "")

        // Crear una nueva instancia del fragmento
        val fragment = BarTopReturn().apply {
            arguments = Bundle().apply {
                putString("usuarioLogeado", usuarioLogeado) // Pasa el usuario logueado como argumento
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        /**/
    }
}