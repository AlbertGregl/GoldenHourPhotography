package hr.gregl.goldenhourphotography.util

object LocationData {
    private var latitude: Double? = null
    private var longitude: Double? = null

    // Default location is Zagreb, Croatia
    private val defaultLatitude = 45.815399
    private val defaultLongitude = 15.966568

    fun updateLocation(lat: Double, lng: Double) {
        latitude = lat
        longitude = lng
    }

    fun getLatitude(): Double {
        return latitude ?: defaultLatitude
    }

    fun getLongitude(): Double {
        return longitude ?: defaultLongitude
    }

    fun setLatitude(latitude: Double?) {
        this.latitude = latitude
    }

    fun setLongitude(longitude: Double?) {
        this.longitude = longitude
    }
}
