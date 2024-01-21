package hr.gregl.goldenhourphotography.handler

import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.factory.createGetHttpUrlConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

suspend fun downloadAndStoreIcon(context: Context, iconCode: String): String? = withContext(
    Dispatchers.IO) {
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    val filename = "${iconCode}.png"
    val file = createFile(context, filename)

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(iconUrl)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return@withContext file.absolutePath
    } catch (e: Exception) {
        Log.e("WeatherIconHandler", e.toString(), e)
        return@withContext null
    }
}

fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if (file.exists()) file.delete()
    return file
}

