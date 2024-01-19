package hr.gregl.goldenhourphotography.api

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("list") val list: List<WeatherData>
)