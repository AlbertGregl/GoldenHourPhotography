package hr.gregl.goldenhourphotography.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.gregl.goldenhourphotography.handler.DateHandler
import hr.gregl.goldenhourphotography.util.LocationData

class TimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val timeFetcher = TimeFetcher(context)
        val dateHandler = DateHandler()
        val (startDate, endDate) = dateHandler.getStartAndEndDates()

        val latitude = LocationData.getLatitude()
        val longitude = LocationData.getLongitude()

        timeFetcher.fetchItems(latitude, longitude, startDate, endDate)

        return Result.success()
    }
}