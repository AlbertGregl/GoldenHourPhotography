package hr.gregl.goldenhourphotography.handler

import java.text.SimpleDateFormat
import java.util.*

class DateHandler {

    fun getStartAndEndDates(): Pair<String, String> {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        val calendar = Calendar.getInstance()
        val startDate = calendar.time // today
        calendar.add(Calendar.DAY_OF_YEAR, NUMBER_OF_DAYS)
        val endDate = calendar.time

        val startDateString = dateFormat.format(startDate)
        val endDateString = dateFormat.format(endDate)

        return Pair(startDateString, endDateString)
    }

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val NUMBER_OF_DAYS = 4 // this is 5 days total
    }
}