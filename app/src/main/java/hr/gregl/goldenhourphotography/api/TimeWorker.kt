package hr.gregl.goldenhourphotography.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.gregl.goldenhourphotography.handler.DateHandler

class TimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val timeFetcher = TimeFetcher(context)
        val dateHandler = DateHandler()
        val (startDate, endDate) = dateHandler.getStartAndEndDates()

        timeFetcher.fetchItems(45.815399, 15.966568, startDate, endDate)

        return Result.success()
    }
}