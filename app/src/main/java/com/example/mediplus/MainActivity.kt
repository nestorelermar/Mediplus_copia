package com.example.mediplus

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: LinearLayout
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recuperar el userId de SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null) ?: ""

        /* Codigo para traer las iniciales del usuario logueado*/
        val usuarioLogeado = sharedPreferences.getString("usuarioLogeado", "")

        // Encontrar el botón en el layout
        val buttonMenu: Button = findViewById(R.id.button_perfil_sesion)

        // Asignar las iniciales al texto del botón
        buttonMenu.text = usuarioLogeado
        /**/
        // Código para el popup de perfil y cerrar sesión
        buttonMenu.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.popup_perfil, null)

            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            // Ajusta el ancho del popup manualmente si lo necesitas
            popupWindow.width = 270 // Ajusta el ancho en píxeles o dp

            // Maneja los clics en las opciones
            popupView.findViewById<TextView>(R.id.verPerfil).setOnClickListener {
                val intent = Intent(this, Perfil::class.java)
                startActivity(intent)
                popupWindow.dismiss()
            }

            popupView.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener {
                Toast.makeText(this, "Eliminar seleccionado", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            // Posicionar el popup alineado a la derecha del botón
            val offsetX = buttonMenu.width - popupWindow.width
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.showAsDropDown(buttonMenu, offsetX, 50)
            } else {
                popupWindow.showAsDropDown(buttonMenu)
            }
        }
        /**/

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


        //llamarTopBar()

        drawerLayout = findViewById(R.id.drawer_layout_main)
        navigationView = findViewById(R.id.navigation_view_main)
        toolbar = findViewById(R.id.menu_lateral_main)
        //val menuIcon: ImageView = findViewById(R.id.icono_de_menu)

        // Configuración del Navigation Drawer
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar clic en los ítems del menú
            when (menuItem.itemId) {
                R.id.tomaMedicamentosMenu -> {
                    startActivity(Intent(this, ModuloMedicamentos::class.java))
                }
                R.id.alertaAbastecimientoMenu -> {
                    startActivity(Intent(this, ModuloAlertaAbastecimiento::class.java))
                }
                R.id.citasMedicasMenu -> {
                    startActivity(Intent(this, ModuloCitas::class.java))
                }
                R.id.gestionSaludMenu -> {
                    startActivity(Intent(this, ModuloGestionSalud::class.java))
                }
                R.id.vidaSaludableMenu -> {
                    startActivity(Intent(this, ModuloVidaSaludable::class.java))
                }
                R.id.examenesMenu -> {
                    Toast.makeText(this, "Examenes todavia no esta implementada", Toast.LENGTH_SHORT).show()
                }
                R.id.historialesMenu -> {
                    startActivity(Intent(this, ModuloHistoriales::class.java))
                }
                R.id.notasMedicamentosMenu -> {
                    Toast.makeText(this, "Notas de medicamentos todavia no esta implementada", Toast.LENGTH_SHORT).show()
                }
                R.id.otrasNotasMenu -> {
                    Toast.makeText(this, "Otras notas generales todavia no esta implementada", Toast.LENGTH_SHORT).show()
                }
            }
            // Cerrar el drawer después de seleccionar un ítem
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Abrir el Navigation Drawer al hacer clic en el icono del menú
        toolbar.setOnClickListener {
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

    /*fun llamarTopBar() {
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
    }*/
}