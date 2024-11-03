package com.example.mediplus

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CrearModuloAbastecimiento : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var editTextDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_modulo_abastecimiento)

        // Recuperar el userId de SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null) ?: ""

        // Código para traer las iniciales del usuario logueado
        val usuarioLogeado = sharedPreferences.getString("usuarioLogeado", "")

        // Crear una nueva instancia del fragmento y pasar el usuario logueado como argumento
        val fragment = BarTopReturn().apply {
            arguments = Bundle().apply {
                putString("usuarioLogeado", usuarioLogeado)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        // Para el selector de fecha
        editTextDate = findViewById(R.id.txtFechaAbastecimiento)
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }

        // Acción para abrir el diálogo al hacer clic en el MaterialAutoCompleteTextView
        findViewById<MaterialAutoCompleteTextView>(R.id.txtMedicamentoAbastecimiento).setOnClickListener {
            obtenerMedicamento()
        }

        // Accion para ir a crear un recordatorio de medicamento
        findViewById<LinearLayout>(R.id.bottonIrCrearRecordatorioMedicamentos).setOnClickListener {
            startActivity(Intent(this, CrearModuloMedicamentos::class.java))
        }

        // Accion para ir a crear un recordatorio de medicamento
        findViewById<LinearLayout>(R.id.btnCrearRecordatorioAbastecer).setOnClickListener {
            registrarAbastecimiento()
        }
    }

    // Método para mostrar el selector de fechas
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun obtenerMedicamento() {
        // Inflar el diseño del diálogo
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_medicamento, null)

        // Crear el diálogo
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Referencias a los elementos del diálogo
        val radioGroupMedicamentos = dialogView.findViewById<RadioGroup>(R.id.radioGroupMedicamentos)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        val btnOk = dialogView.findViewById<TextView>(R.id.btnOk)

        // Obtener datos desde Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("toma_medicamentos") // Cambia al nombre correcto de tu colección
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nombreMedicamento = document.getString("nombre_medicamento") ?: "Sin nombre" // Cambia al nombre correcto de tu campo
                    val radioButton = RadioButton(this)
                    radioButton.text = nombreMedicamento
                    radioGroupMedicamentos.addView(radioButton)
                }
            }
            .addOnFailureListener { e ->
                // Mostrar error en caso de fallo en la obtención de datos
                Toast.makeText(this, "Error al obtener datos: ${e.message}", Toast.LENGTH_LONG).show()
            }

        // Manejar el botón Cancelar
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Manejar el botón OK
        btnOk.setOnClickListener {
            val selectedId = radioGroupMedicamentos.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedRadioButton = dialogView.findViewById<RadioButton>(selectedId)
                val medicamentoSeleccionado = selectedRadioButton.text.toString()
                // Realizar acción con el medicamento seleccionado
                //Toast.makeText(this, "Medicamento seleccionado: $medicamentoSeleccionado", Toast.LENGTH_SHORT).show()
                val opcionSeleccionada = findViewById<MaterialAutoCompleteTextView>(R.id.txtMedicamentoAbastecimiento)
                opcionSeleccionada.setText(medicamentoSeleccionado, false)

            } else {
                Toast.makeText(this, "Por favor selecciona un medicamento", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        // Mostrar el diálogo
        dialog.show()
    }

    private fun registrarAbastecimiento() {
        val db = FirebaseFirestore.getInstance()

        // Obtener los nuevos datos de los EditText (cambia los IDs según tu layout)
        val medicamento = findViewById<EditText>(R.id.txtMedicamentoAbastecimiento).text.toString()
        val fecha_abastecimiento = findViewById<EditText>(R.id.txtFechaAbastecimiento).text.toString()
        val cantidad = findViewById<EditText>(R.id.txtCantidad).text.toString()

        // Crear una referencia al documento de usuario
        val usuarioRef = db.document("/users/$userId")

        // Crear un mapa con los datos a registrar en Firestore
        val medicamentoData = hashMapOf(
            "id_usuario" to usuarioRef,  // Usamos la referencia en lugar de solo el userId
            "medicamento" to medicamento,
            "fecha_abastecimiento" to fecha_abastecimiento,
            "cantidad" to cantidad
        )

        // Registrar el documento del medicamento en Firestore
        db.collection("abastecimiento_medicamentos")
            .add(medicamentoData)  // Usa .add para generar un ID único automáticamente
            .addOnSuccessListener { documentReference ->
                // Redirigir a la actividad de confirmación si no se actualiza la contraseña
                redirectToConfirmation()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar el abastecimiento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    // Método para redirigir a la actividad de confirmación
    private fun redirectToConfirmation() {
        startActivity(Intent(this, ConfirmacionAbastecimientoMedicamento::class.java))
        finish()
    }
}
