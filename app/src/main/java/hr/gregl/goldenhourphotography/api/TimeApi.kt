package hr.gregl.goldenhourphotography.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.sunrisesunset.io/"
interface TimeApi {
    @GET("json")
    fun fetchItems(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("date_start") dateStart: String,
        @Query("date_end") dateEnd: String
    ): Call<ApiResponse>
    @GET("json")
    fun fetchItem(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<ApiResponse>
}



