package hr.gregl.goldenhourphotography.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.TIME_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.TimeReceiver
import hr.gregl.goldenhourphotography.framework.sendBroadcast
import hr.gregl.goldenhourphotography.handler.downloadAndStore
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

    fun fetchItems(count: Int) {

        val request = timeApi.fetchItems(count = 10)

        request.enqueue(object : Callback<List<TimeItem>> {
            override fun onResponse(
                call: Call<List<TimeItem>>,
                response: Response<List<TimeItem>>
            ) {
                response.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<List<TimeItem>>, t: Throwable) {
                Log.e(javaClass.name, t.toString(), t)
            }

        })

    }

    private fun populateItems(timeItems: List<TimeItem>) {
        // FOREGROUND - do not go to internet!!!
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            //BACKROUND
            timeItems.forEach {
                val picturePath = downloadAndStore(context, it.url)

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::explanation.name, it.explanation)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::date.name, it.date)
                    put(Item::read.name, false)
                }

                context.contentResolver.insert(TIME_PROVIDER_CONTENT_URI, values)

            }
            context.sendBroadcast<TimeReceiver>()
        }
    }
}