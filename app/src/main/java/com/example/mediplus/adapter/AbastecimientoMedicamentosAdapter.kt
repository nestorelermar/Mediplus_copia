package com.example.mediplus.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediplus.ConfirmacionArchivoAbastecimientoMedicamento
import com.example.mediplus.R
import com.example.mediplus.ReabastecerMedicamentos
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

data class AbastecimientoMedicamento(
    val id_usuario: String,
    val nombre_medicamento: String,
    val fecha_abastecimiento: String,
    val cantidad: String
)

class AbastecimientoMedicamentoAdapter(private val abastecimiento: List<AbastecimientoMedicamento>) : RecyclerView.Adapter<AbastecimientoMedicamentoAdapter.AbastecimientoMedicamentoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbastecimientoMedicamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_listar_abastecimiento, parent, false)
        return AbastecimientoMedicamentoViewHolder(view)
    }

    class AbastecimientoMedicamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.txtMedicamentoAlertaAbastecimiento)
        private val fechaTextView: TextView = itemView.findViewById(R.id.txtFechaAbastecimientoMedicamentos)
        private val dosisTextView: TextView = itemView.findViewById(R.id.txtUnidades)
        val buttonMenuPuntos: MaterialCardView = itemView.findViewById(R.id.btn_reabastecer)
        val buttonMas: MaterialCardView = itemView.findViewById(R.id.btn_mas)

        fun bind(abastecer: AbastecimientoMedicamento) {
            nombreTextView.text = abastecer.nombre_medicamento
            fechaTextView.text = abastecer.fecha_abastecimiento
            dosisTextView.text = abastecer.cantidad
        }
    }
    override fun onBindViewHolder(holder: AbastecimientoMedicamentoViewHolder, position: Int) {
        holder.bind(abastecimiento[position])

        // Manejo del clic en el botón de tres puntos
        holder.buttonMenuPuntos.setOnClickListener { view ->
            val context = holder.itemView.context
            val medicamento = abastecimiento[position]

            // Crear un Intent para redirigir a la nueva Activity
            val intent = Intent(context, ReabastecerMedicamentos::class.java).apply {
                putExtra("id_usuario", medicamento.id_usuario)
                putExtra("medicamento", medicamento.nombre_medicamento)
                putExtra("cantidad", medicamento.cantidad)
            }

            // Iniciar la nueva Activity
            context.startActivity(intent)
        }

        // Manejo del clic en el botón de tres puntos
        holder.buttonMas.setOnClickListener { view ->
            showPopup(view, holder.itemView.context, abastecimiento[position], holder.buttonMas)
        }
    }

    override fun getItemCount(): Int = abastecimiento.size

    // Método para mostrar el popup
    private fun showPopup(view: View, context: Context, abastecimiento: AbastecimientoMedicamento, buttonMenu: MaterialCardView) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_abastecimiento, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Ajusta el ancho del popup manualmente si lo necesitas
        popupWindow.width = 250 // Puedes ajustar el ancho en píxeles o dp

        popupView.findViewById<TextView>(R.id.eliminar_abastecimiento).setOnClickListener {
            //Toast.makeText(context, "Eliminar seleccionado: ${medicamento.id_usuario}", Toast.LENGTH_SHORT).show()
            showDeleteConfirmationDialog(context) {
                // Eliminar el medicamento de Firestore
                eliminarAbastecimiento(context, abastecimiento.id_usuario)
            }
            popupWindow.dismiss()
        }
        popupView.findViewById<TextView>(R.id.archivar_abastecimiento).setOnClickListener {
            //Toast.makeText(context, "Archivar", Toast.LENGTH_SHORT).show()
            showArchiveConfirmationDialog(context) {
                archivarAbastecimiento(context, abastecimiento)
            }
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

    // Ventana Modal de archivar
    private fun showArchiveConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmar_archivar_abastecimiento, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.btn_cancelar_abastecimiento).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_archivar_abastecimiento).setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.show()
    }

    // Ventana Modal de eliminar
    private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmar_eliminar, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.show()
    }

    // Método para eliminar una actividad de Firestore y de la lista
    private fun eliminarAbastecimiento(context: Context, id_usuario: String) {
        val db = FirebaseFirestore.getInstance()

        // Elimina el documento en Firestore
        db.collection("abastecimiento_medicamentos").document(id_usuario).delete()
            .addOnSuccessListener {
                // El documento se eliminó correctamente
                val nuevoListaAbastecimiento = abastecimiento.filter { it.id_usuario != id_usuario }
                (abastecimiento as MutableList).clear()
                (abastecimiento as MutableList).addAll(nuevoListaAbastecimiento)
                notifyDataSetChanged()
                //Toast.makeText(context, "Actividad eliminada de Firestore", Toast.LENGTH_SHORT).show()
                val confirm = Intent(context, ConfirmacionArchivoAbastecimientoMedicamento::class.java)
                context.startActivity(confirm)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al eliminar el abastecimiento: $e", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para archivar un abastecimiento
    private fun archivarAbastecimiento(context: Context, abastecimiento: AbastecimientoMedicamento) {
        val db = FirebaseFirestore.getInstance()

        // Crear un mapa con los datos a archivar en Firestore
        val medicamentoData = hashMapOf(
            "id_usuario" to abastecimiento.id_usuario,  // Guardar el ID del usuario
            "medicamento" to abastecimiento.nombre_medicamento,
            "fecha_abastecimiento" to abastecimiento.fecha_abastecimiento,
            "cantidad" to abastecimiento.cantidad
        )

        // Registrar el documento del medicamento en Firestore
        db.collection("archivar_abastecimiento")
            .add(medicamentoData)  // Usa .add para generar un ID único automáticamente
            .addOnSuccessListener { documentReference ->
                //Toast.makeText(context, "Medicamento archivado con éxito: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                // Eliminar el documento de la colección 'abastecimiento_medicamentos'
                eliminarMedicamentoDeToma(abastecimiento.id_usuario)
                redirectToConfirmation(context)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al archivar medicamento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para eliminar un medicamento de la colección 'abastecimiento_medicamentos'
    private fun eliminarMedicamentoDeToma(medicamentoId: String) {
        val db = FirebaseFirestore.getInstance()

        // Referencia al documento que se va a eliminar en la colección 'abastecimiento_medicamentos'
        val documentoRef = db.collection("abastecimiento_medicamentos").document(medicamentoId)

        documentoRef.delete()
            .addOnSuccessListener {
                // Mensaje de éxito al eliminar
                // No es necesario hacer nada aquí, ya que lo manejamos en archivarAbastecimiento
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, "Error al eliminar medicamento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para redirigir a la actividad de confirmación
    private fun redirectToConfirmation(context: Context) {
        context.startActivity(Intent(context, ConfirmacionArchivoAbastecimientoMedicamento::class.java))
    }


}