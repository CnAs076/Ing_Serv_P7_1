package es.uniovi.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _exchangeStatus = MutableLiveData<RetrofitClient.ExchangeStatus>(
        RetrofitClient.ExchangeStatus(
            1.16,
            ""
        )
    )

    val exchangeStatus: LiveData<RetrofitClient.ExchangeStatus> = _exchangeStatus

    var yaDescargado: Boolean = false

    init {
        Log.d("MainViewModel", "ViewModel created!")
    }

    fun fetchExchangeRate() {
        if (yaDescargado) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.convert("EUR", "USD", 1.0)
                val body = response.body()

                if (response.isSuccessful && body != null) {

                    val nuevoEstado = RetrofitClient.ExchangeStatus(
                        rate = body.rates.USD,
                        date = body.date
                    )

                    _exchangeStatus.postValue(nuevoEstado) // ¡Notifica a la vista!

                    yaDescargado = true
                    Log.d("MainViewModel", "Datos actualizados: $nuevoEstado")
                } else {
                    Log.e("MainViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Excepción", e)
            }
        }
    }
}