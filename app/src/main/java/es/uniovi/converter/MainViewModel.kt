package es.uniovi.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var euroToDollar: Double = 1.16
    var yaDescargado: Boolean = false

    init {
        Log.d("MainViewModel", "ViewModel created!")
    }

    fun fetchExchangeRate() {
        if (yaDescargado) {
            Log.d("MainViewModel", "Datos ya descargados previamente. No hago nada.")
            return
        }

        Log.d("MainViewModel", "Iniciando descarga de datos...")

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.convert("EUR", "USD", 1.0)
                val exchangeRateResponse = response.body()

                if (response.isSuccessful && exchangeRateResponse != null) {
                    euroToDollar = exchangeRateResponse.rates.USD
                    yaDescargado = true
                    Log.d("MainViewModel", "Tasa actualizada en ViewModel: $euroToDollar")
                } else {
                    Log.e("MainViewModel", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Excepción al obtener datos", e)
            }
        }
    }
}