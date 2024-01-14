package hr.gregl.goldenhourphotography.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.TIME_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.TimeReceiver
import hr.gregl.goldenhourphotography.framework.sendBroadcast
import hr.gregl.goldenhourphotography.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TimeFetcher(private val context: Context) {

    private val timeApi: TimeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        timeApi = retrofit.create(TimeApi::class.java)
    }


    fun fetchItems(latitude: Double, longitude: Double, dateStart: String, dateEnd: String) {
        val request = timeApi.fetchItems(latitude, longitude, dateStart, dateEnd)

        // TODO Debug log for the request URL
        Log.d("TimeFetcher", "Request URL: ${request.request().url()}")

        request.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        // TODO Debug log for successful response
                        Log.d("TimeFetcher", "Successful response: $apiResponse")
                        populateItems(apiResponse.results)
                    }
                } else {
                    Log.e("TimeFetcher", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("TimeFetcher", "API call failed", t)
            }
        })
    }

    private fun populateItems(timeItems: List<TimeItem>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            timeItems.forEach { timeItem ->
                val values = ContentValues().apply {
                    put(Item::date.name, timeItem.date)
                    put(Item::sunrise.name, timeItem.sunrise)
                    put(Item::sunset.name, timeItem.sunset)
                    put(Item::firstLight.name, timeItem.firstLight)
                    put(Item::lastLight.name, timeItem.lastLight)
                    put(Item::dawn.name, timeItem.dawn)
                    put(Item::dusk.name, timeItem.dusk)
                    put(Item::solarNoon.name, timeItem.solarNoon)
                    put(Item::goldenHour.name, timeItem.goldenHour)
                    put(Item::dayLength.name, timeItem.dayLength)
                    put(Item::timezone.name, timeItem.timezone)
                    put(Item::utcOffset.name, timeItem.utcOffset)
                }
                context.contentResolver.insert(TIME_PROVIDER_CONTENT_URI, values)
            }
            context.sendBroadcast<TimeReceiver>()
        }
    }

/*    fun fetchItem(latitude: Double, longitude: Double) {
        val request = timeApi.fetchItems(latitude, longitude)

        request.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        populateItem(apiResponse.results)
                    }
                } else {
                    Log.e("TimeFetcher", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("TimeFetcher", "API call failed", t)
            }
        })
    }


    private fun populateItem(timeItem: TimeItem) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val values = ContentValues().apply {
                put(Item::date.name, timeItem.date)
                put(Item::sunrise.name, timeItem.sunrise)
                put(Item::sunset.name, timeItem.sunset)
                put(Item::firstLight.name, timeItem.firstLight)
                put(Item::lastLight.name, timeItem.lastLight)
                put(Item::dawn.name, timeItem.dawn)
                put(Item::dusk.name, timeItem.dusk)
                put(Item::solarNoon.name, timeItem.solarNoon)
                put(Item::goldenHour.name, timeItem.goldenHour)
                put(Item::dayLength.name, timeItem.dayLength)
                put(Item::timezone.name, timeItem.timezone)
                put(Item::utcOffset.name, timeItem.utcOffset)
            }
            context.contentResolver.insert(TIME_PROVIDER_CONTENT_URI, values)
            context.sendBroadcast<TimeReceiver>()
        }
    }*/

}