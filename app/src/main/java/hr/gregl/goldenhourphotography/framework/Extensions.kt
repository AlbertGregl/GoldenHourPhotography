package hr.gregl.goldenhourphotography.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import hr.gregl.goldenhourphotography.DATA_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item


fun View.applyAnimation(animationId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean = true)
        = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
    .edit()
    .putBoolean(key, value)
    .apply()

fun Context.getBooleanPreference(key: String)
        = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
    .getBoolean(key, false)

fun callDelayed(delay: Long, work: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let {network ->
        connectivityManager.getNetworkCapabilities(network)?.let {networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
    return false
}

@SuppressLint("Range")
fun Context.fetchItems() : MutableList<Item> {
    val items = mutableListOf<Item>()

    val cursor = contentResolver.query(
        DATA_PROVIDER_CONTENT_URI, null, null, null, null
    )
    while (cursor != null && cursor.moveToNext()){
        items.add(
            Item(
                _id = cursor.getLong(cursor.getColumnIndexOrThrow(Item::_id.name)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(Item::date.name)),
                sunrise = cursor.getString(cursor.getColumnIndexOrThrow(Item::sunrise.name)),
                sunset = cursor.getString(cursor.getColumnIndexOrThrow(Item::sunset.name)),
                firstLight = cursor.getString(cursor.getColumnIndexOrThrow(Item::firstLight.name)),
                lastLight = cursor.getString(cursor.getColumnIndexOrThrow(Item::lastLight.name)),
                dawn = cursor.getString(cursor.getColumnIndexOrThrow(Item::dawn.name)),
                dusk = cursor.getString(cursor.getColumnIndexOrThrow(Item::dusk.name)),
                solarNoon = cursor.getString(cursor.getColumnIndexOrThrow(Item::solarNoon.name)),
                goldenHour = cursor.getString(cursor.getColumnIndexOrThrow(Item::goldenHour.name)),
                dayLength = cursor.getString(cursor.getColumnIndexOrThrow(Item::dayLength.name)),
                timezone = cursor.getString(cursor.getColumnIndexOrThrow(Item::timezone.name)),
                utcOffset = cursor.getInt(cursor.getColumnIndexOrThrow(Item::utcOffset.name)),
                temperature = cursor.getDouble(cursor.getColumnIndexOrThrow(Item::temperature.name)),
                weatherIcon = cursor.getString(cursor.getColumnIndexOrThrow(Item::weatherIcon.name)),
                weatherDateTimeText = cursor.getString(cursor.getColumnIndexOrThrow(Item::weatherDateTimeText.name))
            )
        )
    }
    // TODO memory management
    //cursor?.close()
    return items
}