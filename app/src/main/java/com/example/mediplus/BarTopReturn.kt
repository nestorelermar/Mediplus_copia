package com.example.mediplus

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast

class BarTopReturn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_bar_top_return, container, false)

        // Obtener las iniciales pasadas desde la actividad
        val usuarioLogeado = arguments?.getString("usuarioLogeado")

        // Encontrar el botón en el layout
        val buttonMenu: Button = view.findViewById(R.id.button_perfil_cerrar_sesion)

        // Asignar las iniciales al texto del botón
        buttonMenu.text = usuarioLogeado

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
                val intent = Intent(context, Perfil::class.java)
                startActivity(intent)
                popupWindow.dismiss()
            }

            popupView.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener {
                Toast.makeText(context, "Cerrar sesión seleccionado", Toast.LENGTH_SHORT).show()
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

        val boton_regregar = view.findViewById<ImageView>(R.id.bottom_return)
        boton_regregar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = BarTopReturn()
    }
}