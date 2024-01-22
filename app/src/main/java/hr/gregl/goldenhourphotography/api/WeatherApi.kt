package hr.gregl.goldenhourphotography.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_KEY = "b1ac212590e723ad7c5cbddd1ea83b2c"
const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"

interface WeatherApi {
    @GET("forecast")
    fun fetchWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = WEATHER_API_KEY
    ): Call<WeatherResponse>
}