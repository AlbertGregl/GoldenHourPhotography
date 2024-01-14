package hr.gregl.goldenhourphotography.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("results") val results: List<TimeItem>,
    @SerializedName("status") val status: String
)
