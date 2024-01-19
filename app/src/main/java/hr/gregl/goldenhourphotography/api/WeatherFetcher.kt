package hr.gregl.goldenhourphotography.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.DATA_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFetcher(private val context: Context) {
    private val weatherApi: WeatherApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        val request = weatherApi.fetchWeather(latitude, longitude)

        // TODO Debug log for the request URL
        Log.d("WeatherFetcher", "Request URL: ${request.request().url()}")

        request.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        // TODO Debug log for successful response
                        Log.d("WeatherFetcher", "Successful response: $weatherResponse")
                        val weatherData = processWeatherResponse(weatherResponse)
                        updateDatabaseWithWeatherData(weatherData)
                    }
                } else {
                    Log.e(
                        "WeatherFetcher",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherFetcher", "API call failed", t)
            }

            private fun processWeatherResponse(weatherResponse: WeatherResponse): List<WeatherData> {
                return weatherResponse.list.map { weatherData ->
                    WeatherData(
                        main = weatherData.main,
                        weather = weatherData.weather,
                        dateTimeText = weatherData.dateTimeText
                    )
                }
            }

            private fun updateDatabaseWithWeatherData(weatherData: List<WeatherData>) {

                val weatherDataByDate = weatherData.groupBy {
                    it.dateTimeText.substringBefore(' ')
                }
                // TODO Debug log for grouped weather data
                Log.d("WeatherUpdate", "Grouped Weather Data: $weatherDataByDate")

                weatherDataByDate.forEach { (date, dataList) ->
                    // Find the first data point for this date that is at or after 18:00:00
                    val relevantData = dataList.firstOrNull { data ->
                        val timePart = data.dateTimeText.substringAfter(' ')
                        timePart >= "18:00:00"
                    } ?: dataList.first()

                    // TODO Debug log for relevant data
                    Log.d("WeatherUpdate", "Updating Date: $date, Data of date: ${relevantData.dateTimeText}")

                    val values = ContentValues().apply {
                        put("temperature", relevantData.main.temp)
                        put("weatherIcon", relevantData.weather.firstOrNull()?.icon)
                        put("weatherDateTimeText", relevantData.dateTimeText)
                    }

                    // Update the database where date matches
                    val selection = "${Item::date.name} = ?"
                    val selectionArgs = arrayOf(date)

                    val updateCount = context.contentResolver.update(
                        DATA_PROVIDER_CONTENT_URI,
                        values,
                        selection,
                        selectionArgs
                    )

                    // TODO Debug log for updated rows
                    Log.d("WeatherUpdate", "Updated rows for $date: $updateCount")
                }
            }

        })
    }
}