package com.example.mediplus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar

class EditarModuloExamenes : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var editTextDate: EditText
    private lateinit var btnSelectTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_modulo_examenes)

        // Recuperar el userId de SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null) ?: ""

        /* Codigo para traer las iniciales del usuario logueado*/
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

        // Obtener los datos del Intent
        val id_usuario = intent.getStringExtra("id_usuario")
        val nombre_examen = intent.getStringExtra("nombre_examen")
        val fecha = intent.getStringExtra("fecha")
        val hora = intent.getStringExtra("hora")
        val especialidad = intent.getStringExtra("especialidad")
        val entidad = intent.getStringExtra("entidad")
        val nombre_doctor = intent.getStringExtra("nombre_doctor")
        val descripcion = intent.getStringExtra("descripcion")

        // Asignar los datos a los TextViews
        findViewById<TextView>(R.id.txtExamenEditar).text = nombre_examen
        findViewById<TextView>(R.id.txtFechaExamenEditar).text = fecha
        findViewById<TextView>(R.id.txtHoraExamenEditar).text = hora
        findViewById<TextView>(R.id.txtEspecialidadExamenEditar).text = especialidad
        findViewById<TextView>(R.id.txtEntidadEditar).text = entidad
        findViewById<TextView>(R.id.txtNombreDoctorEditar).text = nombre_doctor
        findViewById<TextView>(R.id.txtDescripcionExamenEditar).text = descripcion

        /**/

        // Accion para el select de bienestar
        val seleccionEspecialidad: MaterialAutoCompleteTextView = findViewById(R.id.txtEspecialidadExamenEditar)

        // Define las opciones del dropdown
        val opcionesEspecialidad = arrayOf("Laboratorios", "opcion 2", "opcion 3", "opcion 4", "Otra medida")

        // Crea un ArrayAdapter con las opciones
        val adapterArray = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesEspecialidad)

        // Asigna el adaptador al MaterialAutoCompleteTextView
        seleccionEspecialidad.setAdapter(adapterArray)

        /**/

        // Para el selector de fecha
        editTextDate = findViewById(R.id.txtFechaExamenEditar)

        // Configurar el listener para el EditText
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }

        /**/

        //Para el selector de hora TimePickerDialog
        btnSelectTime = findViewById(R.id.txtHoraExamenEditar)

        btnSelectTime.setOnClickListener {
            showTimePickerDialog(btnSelectTime)
        }

        /**/
        // Accion de registrar una actividad
        findViewById<LinearLayout>(R.id.btnEditarExamen).setOnClickListener {
            if (id_usuario != null) {
                editarExamen(id_usuario)
            }else{
                Toast.makeText(this, "Error: ${id_usuario} no encontrado", Toast.LENGTH_LONG).show()
            }
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

    // Metodo para llamar el selector de hora
    private fun showTimePickerDialog(editText: EditText) {
        // Obtener la hora actual
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crear el TimePickerDialog
        val timePickerDialog = TimePickerDialog(this, //R.style.CustomTimePickerDialog,
            { _, selectedHour, selectedMinute ->
                // Convertir la hora a formato de 12 horas
                val amPm = if (selectedHour >= 12) "pm" else "am"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12

                // Mostrar la hora seleccionada con AM o PM
                editText.setText(String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm))
            }, hour, minute, false) // false para formato de 12 horas

        // Mostrar el diálogo
        try {
            timePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace() // Capturar cualquier excepción
        }
    }

    // Metodo de editar examen
    private fun editarExamen(examenId: String) {
        val db = FirebaseFirestore.getInstance()

        try {
            val nombreExamenActualizado = findViewById<EditText>(R.id.txtExamenEditar).text.toString()
            val fechaActualizada = findViewById<EditText>(R.id.txtFechaExamenEditar).text.toString()
            val horaActualizada = findViewById<EditText>(R.id.txtHoraExamenEditar).text.toString()
            val especialidadActualizada = findViewById<EditText>(R.id.txtEspecialidadExamenEditar).text.toString()
            val entidadActualizada = findViewById<EditText>(R.id.txtEntidadEditar).text.toString()
            val nombreDoctorActualizada = findViewById<TextView>(R.id.txtNombreDoctorEditar).text.toString()
            val descripcionActualizada = findViewById<TextView>(R.id.txtDescripcionExamenEditar).text.toString()

            if (examenId.isEmpty()) {
                Toast.makeText(this, "ID del medicamento no válido", Toast.LENGTH_SHORT).show()
                return
            }

            val medicamentoActualizado = hashMapOf(
                "nombre_examen" to nombreExamenActualizado,
                "fecha" to fechaActualizada,
                "hora" to horaActualizada,
                "especialidad" to especialidadActualizada,
                "entidad" to entidadActualizada,
                "nombre_doctor" to nombreDoctorActualizada,
                "descripcion" to descripcionActualizada
            )

            db.collection("examenes").document(examenId)
                .set(medicamentoActualizado, SetOptions.merge())
                .addOnSuccessListener {
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
        startActivity(Intent(this, ConfirmacionEdicionExamen::class.java))
        finish()
    }
}