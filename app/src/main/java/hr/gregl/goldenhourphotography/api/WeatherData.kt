package hr.gregl.goldenhourphotography.api

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("main") val main: WeatherMain,
    @SerializedName("weather") val weather: List<WeatherDescription>,
    @SerializedName("dt_txt") val dateTimeText: String
)