package hr.gregl.goldenhourphotography.api

import com.google.gson.annotations.SerializedName

data class WeatherMain(
    @SerializedName("temp") val temp: Double
)