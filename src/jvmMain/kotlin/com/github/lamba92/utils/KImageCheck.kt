@file:JvmName("KImageCheck")
package com.github.lamba92.utils

import java.io.EOFException
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import javax.imageio.IIOException
import javax.imageio.ImageIO

/**
 * Analyzes the file at this [path] to check if it is an image and if so if it truncated or not.
 * @param path The path to the file to be analyzed.
 * @return An [ImageData] instance.
 */
actual fun analyzeImage(path: String): ImageData {
    val file = File(path)
    val isTruncated = try {
        file.inputStream().use { digestInputStream ->
            ImageIO.createImageInputStream(digestInputStream).use { imageInputStream ->
                ImageIO.getImageReaders(imageInputStream)
                    .apply { if (!hasNext()) return ImageData(false) }
                    .next()
                    .run {
                        input = imageInputStream
                        read(0)?.flush() ?: return ImageData(false)
                        if (formatName == "JPEG") {
                            imageInputStream.seek(imageInputStream.streamPosition - 2)
                            val lastTwoBytes = ByteArray(2)
                            imageInputStream.read(lastTwoBytes)
                            lastTwoBytes[0] != 0xff.toByte() || lastTwoBytes[1] != 0xd9.toByte()
                        } else false
                    }
            }
        }
    } catch (e: IndexOutOfBoundsException) {
        true
    } catch (e: IIOException){
        e.cause is EOFException
    } catch (e: FileNotFoundException){
        return ImageData(false)
    }
    return ImageData(true, isTruncated)
}

/**
 * Analyzes [path] to check if it is an image and if so if it truncated or not.
 * @param path The [File] to be analyzed.
 * @return An [ImageData] instance.
 */
fun analyzeImage(path: Path) = analyzeImage(path.toString())

fun File.getImageData() = analyzeImage(this.absolutePath)

fun Path.getImageData() = analyzeImage(this.toString())

actual typealias File = File