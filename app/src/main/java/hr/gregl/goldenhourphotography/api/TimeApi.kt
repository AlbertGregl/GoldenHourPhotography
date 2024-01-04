package hr.gregl.goldenhourphotography.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.nasa.gov/planetary/"

interface TimeApi {
    @GET("apod?api_key=DEMO_KEY")
    fun fetchItem(@Query("count") count: Int = 10)
            : Call<List<TimeItem>>
}