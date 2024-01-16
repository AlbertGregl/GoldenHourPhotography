package hr.gregl.goldenhourphotography.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat


object LocationUtils {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    fun isPermissionGranted(requestCode: Int, grantResults: IntArray): Boolean {
        return requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.isNotEmpty() &&
                grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun promptUserToEnableLocationIfNeeded(activity: Activity) {
        if (!isLocationEnabled(activity)) {
            AlertDialog.Builder(activity)
                .setMessage("Your location setting is turned off. Please enable location services to continue.")
                .setPositiveButton("Settings") { _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    fun checkLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, activity: Activity) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
                    getCurrentLocation(activity)
                } else {
                    Log.d("Location", "Permission denied")
                }
                return
            }
        }
    }

    fun getCurrentLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    LocationData.updateLocation(location.latitude, location.longitude)
                    locationManager.removeUpdates(this)
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        }
    }

}