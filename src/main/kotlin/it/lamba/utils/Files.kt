package it.lamba.utils

import java.io.EOFException
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import javax.imageio.IIOException
import javax.imageio.ImageIO

/**
 * Analyzes [path] to check if it is an image and if so if it truncated or not.
 * @param path The [File] to be analyzed.
 * @return An [ImageData] instance.
 */
fun analyzeImage(path: Path) = analyzeImage(path.toFile())

/**
 * Analyzes [file] to check if it is an image and if so if it truncated or not.
 * @param file The [File] to be analyzed.
 * @return An [ImageData] instance.
 */
fun analyzeImage(file: File): ImageData {
    val isTruncated = try {
        file.inputStream().use { digestInputStream ->
            val imageInputStream = ImageIO.createImageInputStream(digestInputStream)
            imageInputStream.let { ImageIO.getImageReaders(it) }
                .apply { if (!hasNext()) return ImageData(false) }
                .let { it.next() }
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
    } catch (e: IndexOutOfBoundsException) {
        true
    } catch (e: IIOException){
        e.cause is EOFException
    } catch (e: FileNotFoundException){
        return ImageData(false)
    }
    return ImageData(true, isTruncated)
}

data class ImageData(val isImage: Boolean, val isTruncated: Boolean = false)

val File.imageData: ImageData
    get() = analyzeImage(this)

val Path.imageData: ImageData
    get() = analyzeImage(this)