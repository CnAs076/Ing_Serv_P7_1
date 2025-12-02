package es.uniovi.converter

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- MODELOS DE DATOS (Data Classes) ---
// Representan la estructura del JSON: {"rates": {"USD": 1.16}}
data class Rates(
    val USD: Double
)

data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Rates
)

// --- INTERFAZ DE LA API ---
// Define cómo le pedimos los datos al servidor
interface ExchangeRateApi {
    @GET("latest")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<ExchangeRateResponse>
}

// --- CLIENTE RETROFIT (Singleton) ---
// La instancia única que gestiona las conexiones
object RetrofitClient {
    private const val BASE_URL = "https://api.frankfurter.app/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ExchangeRateApi by lazy {
        retrofit.create(ExchangeRateApi::class.java)
    }

    data class ExchangeStatus(
        val rate: Double,
        val date: String
    )
}