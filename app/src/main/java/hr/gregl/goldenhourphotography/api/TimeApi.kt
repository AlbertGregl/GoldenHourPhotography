package hr.gregl.goldenhourphotography.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.sunrisesunset.io/"

interface TimeApi {
    @GET("json")
    fun fetchItems(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<ApiResponse>
}