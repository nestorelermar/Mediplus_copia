package com.example.mediplus

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar

class ReabastecerMedicamentos : AppCompatActivity() {

    private lateinit var editTextDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reabastecer_medicamentos)

        /* Codigo para traer las iniciales del usuario logueado*/
        // Recuperar el userId de SharedPreferences
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

        // Agregar el fragmento de la bottom bar
        val fragmentBar = BarBottom()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_bottom_bar, fragmentBar)
            .commit()

        /**/

        // Para el selector de fecha
        editTextDate = findViewById(R.id.txtNuevaFechaAbastecimiento)

        // Configurar el listener para el EditText
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }

        /**/
        // Obtener los datos del Intent
        val id_usuario = intent.getStringExtra("id_usuario")
        val nombre_medicamento = intent.getStringExtra("medicamento")
        val cantidad = intent.getStringExtra("cantidad")

        if (id_usuario == null || nombre_medicamento == null || cantidad == null) {
            Toast.makeText(this, "Datos faltantes en el Intent", Toast.LENGTH_SHORT).show()
            return
        }

        // Asignar los datos a los TextViews
        findViewById<TextView>(R.id.txtNombreMedicamentoAbastecimiento).text = nombre_medicamento
        findViewById<TextView>(R.id.txtUnidadesAbastecimiento).text = cantidad

        // Accion del boton Guardar Recordatorio
        findViewById<LinearLayout>(R.id.btnReabastecer).setOnClickListener {
            // Supongamos que has pasado el medicamentoId a esta actividad
            if (id_usuario != null) {
                reabastecerCantidadMedicamento(id_usuario)
            }else{
                Toast.makeText(this, "Error: ${id_usuario} no encontrado", Toast.LENGTH_LONG).show()
            }

        }

        // Accion del boton Cancelar
        findViewById<LinearLayout>(R.id.btnCancelarReabastecimiento).setOnClickListener {
            finish()
        }
    }

    // Metodo para llamar el selector de fechas
    private fun showDatePickerDialog(editText: EditText) {
        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Mostrar la fecha seleccionada en el EditText correspondiente
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
        }, year, month, day)

        // Mostrar el dialogo
        datePickerDialog.show()
    }

    // Metodo para reabastecer cantidad medicamento
    private fun reabastecerCantidadMedicamento(medicamentoId: String) {
        val db = FirebaseFirestore.getInstance()

        try {
            val fecha_abastecimiento = findViewById<EditText>(R.id.txtNuevaFechaAbastecimiento).text.toString()
            val cantidad = findViewById<EditText>(R.id.txtNuevaCantidad).text.toString()

            if (medicamentoId.isEmpty()) {
                Toast.makeText(this, "ID del medicamento no válido", Toast.LENGTH_SHORT).show()
                return
            }

            val medicamentoActualizado = hashMapOf(
                "fecha_abastecimiento" to fecha_abastecimiento,
                "cantidad" to cantidad
            )

            db.collection("abastecimiento_medicamentos").document(medicamentoId)
                .set(medicamentoActualizado, SetOptions.merge())
                .addOnSuccessListener {
                    //Toast.makeText(this, "Medicamento actualizado correctamente", Toast.LENGTH_SHORT).show()
                    // Redirigir a la actividad de confirmación si no se actualiza la contraseña
                    redirectToConfirmation()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar datos en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Método para redirigir a la actividad de confirmación
    private fun redirectToConfirmation() {
        startActivity(Intent(this, ConfirmacionAbastecimientoMedicamento::class.java))
        finish()
    }
}