package com.keru.novelly.data.data_source.local.models

import java.io.File

data class DownloadedFile(
    val file: File,
    val readingProgress: Float,
)
