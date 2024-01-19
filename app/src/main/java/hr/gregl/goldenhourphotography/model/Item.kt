package hr.gregl.goldenhourphotography.model

data class Item(
    var _id: Long?,
    val date: String,
    val sunrise: String,
    val sunset: String,
    val firstLight: String,
    val lastLight: String,
    val dawn: String,
    val dusk: String,
    val solarNoon: String,
    val goldenHour: String,
    val dayLength: String,
    val timezone: String,
    val utcOffset: Int,
    val temperature: Double? = -11.1,
    val weatherIcon: String? = "",
    val weatherDateTimeText: String? = ""
)
