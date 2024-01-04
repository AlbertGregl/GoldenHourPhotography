package hr.gregl.goldenhourphotography.handler

import android.content.Context
import android.util.Log
import hr.gregl.goldenhourphotography.factory.createGetHttpConnection
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

fun downloadImageAndStore(context: Context, url: String): String? {

    val filename = url.substring(url.lastIndexOf(File.separatorChar) + 1)
    val file: File = createFile(context, filename)
    try {
        val con: HttpURLConnection = createGetHttpConnection(url)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    } catch (e: Exception){
        Log.e("IMAGE_HANDLER", e.toString(), e)
    }

    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if (file.exists()) file.delete()
    return  file
}
