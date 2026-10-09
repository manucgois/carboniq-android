package br.com.fiap.carboniq.data

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current") val current: CurrentAirQuality
)

data class CurrentAirQuality(
    @SerializedName("time") val time: String,
    @SerializedName("european_aqi") val europeanAqi: Int,
    @SerializedName("pm2_5") val pm25: Double
)