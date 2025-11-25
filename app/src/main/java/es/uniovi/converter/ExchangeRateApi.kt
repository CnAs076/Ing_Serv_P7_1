package es.uniovi.converter

interface ExchangeRateApi {
    @GET("latest")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<ExchangeRateResponse>
}