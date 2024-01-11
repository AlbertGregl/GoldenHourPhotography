package hr.gregl.goldenhourphotography.handler

import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.factory.createGetHttpUrlConnection
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

fun downloadAndStore(context: Context, url: String) : String? {


    val filename = url.substring(url.lastIndexOf(File.separatorChar) + 1)
    val file = createFile(context, filename)

    try {
        // https://apod.nasa.gov/apod/image/1002/ISSEndeavourFlyby2park.jpg
        val con: HttpURLConnection = createGetHttpUrlConnection(url)

        Files.copy(con.inputStream, Paths.get(file.toURI()))

        return file.absolutePath

    } catch (e: Exception) {
        Log.e("IMAGES_HANDLER", e.toString(), e)
    }


    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if(file.exists()) file.delete()
    return file
}
