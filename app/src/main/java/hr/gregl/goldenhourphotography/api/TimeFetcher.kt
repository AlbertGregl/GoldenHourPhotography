package hr.gregl.goldenhourphotography.api

import android.content.Context
import hr.gregl.goldenhourphotography.TimeReceiver
import hr.gregl.goldenhourphotography.framework.sendBroadcast
class TimeFetcher(private val context: Context) {
    fun fetchItems(count: Int) {
        // fake work
        Thread.sleep(6000)
        // kad zavrsi baca broadcast
        context.sendBroadcast<TimeReceiver>()
    }
}