package es.uniovi.converter

data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Rates
)