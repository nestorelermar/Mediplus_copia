package com.example.mediplus

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat

class navigation_drawer : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // Configuración del Navigation Drawer
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar clic en los ítems del menú
            when (menuItem.itemId) {
                R.id.tomaMedicamentos -> {
                    // Lógica para el ítem 1
                }
                R.id.alertaAbastecimiento -> {
                    // Lógica para el ítem 2
                }
                // Agrega más ítems según sea necesario
            }
            // Cerrar el drawer después de seleccionar un ítem
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Manejar el clic del icono del menú en la Toolbar
        toolbar.setNavigationIcon(R.drawable.icono_dropdown) // Asegúrate de tener un icono de menú
        toolbar.setNavigationOnClickListener {
            // Abrir el Navigation Drawer al hacer clic en el icono
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        // Cerrar el drawer si está abierto al presionar el botón de atrás
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}