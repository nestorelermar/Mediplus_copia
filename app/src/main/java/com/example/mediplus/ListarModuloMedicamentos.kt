package com.example.mediplus

import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListarModuloMedicamentos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_modulo_medicamentos)

        val buttonMenu = findViewById<ImageView>(R.id.button_menu)
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
        }



        // Dentro de tu método onCreate o Fragmento
        /*val buttonMenu = findViewById<ImageView>(R.id.button_menu)

        buttonMenu.setOnClickListener { view ->
            // Crear el PopupMenu
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_opciones, popupMenu.menu)

            // Forzar a que se muestren los íconos usando reflexión
            try {
                val fields: Array<Field> = popupMenu.javaClass.declaredFields
                for (field in fields) {
                    if (field.name == "mPopup") {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popupMenu)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Manejar el clic en las opciones del menú
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        // Acción para editar
                        Toast.makeText(this, "Editar seleccionado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.delete -> {
                        // Acción para eliminar
                        Toast.makeText(this, "Eliminar seleccionado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.info -> {
                        // Acción para ver información
                        Toast.makeText(this, "Ver info seleccionado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            // Mostrar el menú
            popupMenu.show()
        }*/

    }
}
