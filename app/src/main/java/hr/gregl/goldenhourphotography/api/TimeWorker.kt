package hr.gregl.goldenhourphotography.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        TimeFetcher(context).fetchItems(10)
        return Result.success()
    }

}