package es.uniovi.converter

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView // Importar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    lateinit var editTextEuros: EditText
    lateinit var editTextDollars: EditText
    lateinit var textViewStatus: TextView // El nuevo campo de texto

    // Variable local para guardar la tasa actual y usarla en los botones
    private var currentRate: Double = 1.16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        editTextEuros = findViewById(R.id.editTextEuros)
        editTextDollars = findViewById(R.id.editTextDollars)
        textViewStatus = findViewById(R.id.textViewStatus) // Enlazar vista

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- PATRÓN OBSERVADOR ---
        // Aquí nos suscribimos a los cambios.
        // "it" contiene el objeto ExchangeStatus que definimos.
        viewModel.exchangeStatus.observe(this) { status ->
            // 1. Actualizamos nuestra variable local para los cálculos
            currentRate = status.rate

            // 2. Actualizamos la interfaz automáticamente
            if (status.date.isNotEmpty()) {
                textViewStatus.text = "Tasa: ${status.rate} (Fecha: ${status.date})"
                Toast.makeText(this, "¡Datos actualizados!", Toast.LENGTH_SHORT).show()
            } else {
                textViewStatus.text = "Usando tasa por defecto: ${status.rate}"
            }
        }

        // Pedimos los datos (si no estaban ya)
        viewModel.fetchExchangeRate()
    }

    private fun convert(source: EditText, destination: EditText, factor: Double) {
        val text = source.text.toString()
        val value = text.toDoubleOrNull()
        if (value == null) {
            destination.setText("")
            return
        }
        destination.setText((value * factor).toString())
    }

    fun onClickToDollars(view: View) {
        // Usamos currentRate, que se mantiene actualizado gracias al Observer
        convert(editTextEuros, editTextDollars, currentRate)
    }

    fun onClickToEuros(view: View) {
        convert(editTextDollars, editTextEuros, 1 / currentRate)
    }
}