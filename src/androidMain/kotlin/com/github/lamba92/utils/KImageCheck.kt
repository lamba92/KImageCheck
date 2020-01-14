package com.github.lamba92.utils

import android.graphics.BitmapFactory
import java.io.File
import java.nio.file.Path

/**
 * Analyzes the file at this [path] to check if it is an image and if so if it truncated or not.
 * @param path The path to the file to be analyzed.
 * @return An [ImageData] instance.
 */
actual fun analyzeImage(path: String): ImageData {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)
    return if (options.outWidth != -1 || options.outHeight != -1) {
        ImageData(isImage = true, isTruncated = false)
    } else
        ImageData(false)
}

actual typealias File = File

/**
 * Analyzes [path] to check if it is an image and if so if it truncated or not.
 * @param path The [File] to be analyzed.
 * @return An [ImageData] instance.
 */
fun analyzeImage(path: Path) = analyzeImage(path.toString())

fun File.getImageData() = analyzeImage(this.absolutePath)

fun Path.getImageData() = analyzeImage(this.toString())