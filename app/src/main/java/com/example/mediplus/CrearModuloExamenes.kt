package com.example.mediplus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CrearModuloExamenes : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var editTextDate: EditText
    private lateinit var btnSelectTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_modulo_examenes)

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

        // Accion para el select de bienestar
        val seleccionEspecialidad: MaterialAutoCompleteTextView = findViewById(R.id.txtEspecialidadExamen)

        // Define las opciones del dropdown
        val opcionesEspecialidad = arrayOf("Laboratorios", "opcion 2", "opcion 3", "opcion 4", "Otra medida")

        // Crea un ArrayAdapter con las opciones
        val adapterArray = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesEspecialidad)

        // Asigna el adaptador al MaterialAutoCompleteTextView
        seleccionEspecialidad.setAdapter(adapterArray)

        /**/

        // Para el selector de fecha
        editTextDate = findViewById(R.id.txtFechaExamen)

        // Configurar el listener para el EditText
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }

        /**/

        //Para el selector de hora TimePickerDialog
        btnSelectTime = findViewById(R.id.txtHoraExamen)

        btnSelectTime.setOnClickListener {
            showTimePickerDialog(btnSelectTime)
        }

        /**/
        // Accion de registrar una actividad
        findViewById<LinearLayout>(R.id.btnCrearExamen).setOnClickListener {
            registrarExamen()
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

    // Metodo para registrar una actividad
    private fun registrarExamen() {
        val db = FirebaseFirestore.getInstance()

        // Obtener los nuevos datos de los EditText (cambia los IDs según tu layout)
        val nombre_examen = findViewById<EditText>(R.id.txtExamen).text.toString()
        val fecha = findViewById<EditText>(R.id.txtFechaExamen).text.toString()
        val hora = findViewById<EditText>(R.id.txtHoraExamen).text.toString()
        val especialidad = findViewById<EditText>(R.id.txtEspecialidadExamen).text.toString()
        val entidad = findViewById<EditText>(R.id.txtEntidad).text.toString()
        val nombre_doctor = findViewById<EditText>(R.id.txtNombreDoctor).text.toString()
        val descripcion = findViewById<EditText>(R.id.txtDescripcionExamen).text.toString()

        // Crear una referencia al documento de usuario
        val usuarioRef = db.document("/users/$userId")

        // Crear un mapa con los datos a registrar en Firestore
        val gestionSaludData = hashMapOf(
            "id_usuario" to usuarioRef,  // Usamos la referencia en lugar de solo el userId
            "nombre_examen" to nombre_examen,
            "fecha" to fecha,
            "hora" to hora,
            "especialidad" to especialidad,
            "entidad" to entidad,
            "nombre_doctor" to nombre_doctor,
            "descripcion" to descripcion
        )

        // Registrar el documento del medicamento en Firestore
        db.collection("examenes")
            .add(gestionSaludData)  // Usa .add para generar un ID único automáticamente
            .addOnSuccessListener { documentReference ->
                // Redirigir a la actividad de confirmación
                redirectToConfirmation()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar la gestion en salud: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    // Método para redirigir a la actividad de confirmación
    private fun redirectToConfirmation() {
        startActivity(Intent(this, ConfirmacionRecordatorioExamen::class.java))
        finish()
    }
}