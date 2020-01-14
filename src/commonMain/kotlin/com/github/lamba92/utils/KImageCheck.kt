package com.github.lamba92.utils

data class ImageData(val isImage: Boolean, val isTruncated: Boolean = false)

expect class File

expect fun analyzeImage(path: String): ImageData