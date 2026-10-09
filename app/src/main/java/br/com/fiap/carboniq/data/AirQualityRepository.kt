package br.com.fiap.carboniq.data

class AirQualityRepository(private val api: AirQualityApi = RetrofitInstance.airQualityApi) {

    suspend fun getCurrentAirQuality(
        latitude: Double = -23.55,
        longitude: Double = -46.63
    ): CurrentAirQuality {
        return api.getAirQuality(latitude, longitude).current
    }
}