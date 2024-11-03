package com.example.mediplus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.mediplus.inicio_sesion.Login

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 3000 // Tiempo de espera (3 segundos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            // Iniciar la actividad de inicio de sesión
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Finaliza el splash screen activity
        }, splashTimeOut)
    }
}
