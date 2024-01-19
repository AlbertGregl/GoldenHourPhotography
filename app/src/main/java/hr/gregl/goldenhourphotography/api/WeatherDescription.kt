package hr.gregl.goldenhourphotography.api

import com.google.gson.annotations.SerializedName

data class WeatherDescription(
    @SerializedName("icon") val icon: String
)