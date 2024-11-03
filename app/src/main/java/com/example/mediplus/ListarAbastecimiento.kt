package com.example.mediplus

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class ListarAbastecimiento : AppCompatActivity() {

    private lateinit var btnMas: MaterialCardView
    private lateinit var layoutExtraButtons: LinearLayout
    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_abastecimiento)

        /*val buttonMenu = findViewById<ImageView>(R.id.button_menu_puntos)
        buttonMenu.setOnClickListener { view ->
            val inflater = layoutInflater
            val popupView = inflater.inflate(R.layout.popup_layout, null)

            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            // Ajusta el ancho del popup manualmente si lo necesitas
            popupWindow.width = 250 // Puedes ajustar el ancho en píxeles o dp

            // Maneja los clics en las opciones
            popupView.findViewById<TextView>(R.id.edit).setOnClickListener {
                Toast.makeText(this, "Editar seleccionado", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }
            popupView.findViewById<TextView>(R.id.delete).setOnClickListener {
                Toast.makeText(this, "Eliminar seleccionado", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }
            popupView.findViewById<TextView>(R.id.info).setOnClickListener {
                Toast.makeText(this, "Ver info seleccionado", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            // Posicionar el popup alineado a la derecha del botón
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Muestra el popup a la derecha del botón
                val offsetX = buttonMenu.width - popupWindow.width
                popupWindow.showAsDropDown(buttonMenu, offsetX, 0)
            } else {
                popupWindow.showAsDropDown(buttonMenu)
            }
        }*/

        // En el método onCreate o donde inicialices los botones

    }
}