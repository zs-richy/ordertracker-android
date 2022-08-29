package hu.unideb.inf.ordertracker_android.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object FileUtils {

    enum class Directory {
        IMAGES
    }

    fun findDirectory(context: Context, directory: Directory): File {
        return File(context.filesDir.path + "/$directory")
    }

    fun findPath(context: Context, directory: Directory, fileName: String): File {
        var file = File(findDirectory(context, directory), fileName)

        return file
    }

    fun writeDataToFile(file: File, data: ByteArray): Boolean {
        try {
            File(file.parent).mkdirs()
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}