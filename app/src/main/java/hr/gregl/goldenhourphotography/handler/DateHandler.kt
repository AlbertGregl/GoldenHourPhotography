package hr.gregl.goldenhourphotography.handler

import java.text.SimpleDateFormat
import java.util.*

class DateHandler {
    fun getStartAndEndDates(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        val startDate = calendar.time // today
        calendar.add(Calendar.DAY_OF_YEAR, 5) // add 5 days
        val endDate = calendar.time

        val startDateString = dateFormat.format(startDate)
        val endDateString = dateFormat.format(endDate)

        return Pair(startDateString, endDateString)
    }
}