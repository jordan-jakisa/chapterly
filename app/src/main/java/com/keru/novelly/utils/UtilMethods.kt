package com.keru.novelly.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun File.getDateCreated(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val path = Paths.get(this.absolutePath)
        val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
        formatDateTime(attrs.creationTime()).toString()
    } else {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(fileTime: FileTime): String? {
    val localDateTime = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"))
}

fun Long.getRelativeTimePast(): String {
    val currentTimeStamp = System.currentTimeMillis()
    val elapsedTime = currentTimeStamp - this

    return when {
        elapsedTime < 5000 -> "Just now"
        elapsedTime < 60000 -> "${elapsedTime / 1000}s ago"
        elapsedTime < 60 * 60 * 1000 -> {
            val minutes = elapsedTime / (60 * 1000)
            "${minutes}m ago"
        }

        elapsedTime < 24 * 60 * 60 * 1000 -> {
            val hours = elapsedTime / (60 * 60 * 1000)
            "${hours}h ago"
        }

        elapsedTime < 3 * 24 * 60 * 60 * 1000 -> {
            val days = elapsedTime / (24 * 60 * 60 * 1000)
            "${days}d ago"
        }

        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = this
            val sdf = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
            sdf.format(calendar.time)
        }
    }
}

fun Context.shareContent(message: String, title: String, book: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "*$title*\n\n" + message + "\n\n\n You can download it here: \n $book"
        )
    }

    val chooserIntent = Intent.createChooser(intent, "Share via")

    if (intent.resolveActivity(this.packageManager) != null) this.startActivity(chooserIntent)
    else this.startActivity(intent)
}

fun Context.shareApp() {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Hola, \nNovelly lets you search and download all your favorite books all for free! \n Download it here: www.google.com"
        )
    }
    val chooserIntent = Intent.createChooser(intent, "Share via")

    if (intent.resolveActivity(this.packageManager) != null) this.startActivity(chooserIntent)
    else this.startActivity(intent)
}

fun Context.toastComingSoon() {
    Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
}

fun Context.sendEmail(subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:info.pdfcafe@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    if (intent.resolveActivity(this.packageManager) != null) this.startActivity(intent)
    else this.startActivity(intent)
}

fun Context.rateApp() {
    val packageName = this.packageName
    val uri = Uri.parse("market://details?id=$packageName")
    val playstoreIntent = Intent(Intent.ACTION_VIEW, uri)
    playstoreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

    try {
        this.startActivity(playstoreIntent)
    } catch (e: ActivityNotFoundException) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
        this.startActivity(webIntent)
    }

}

fun Context.openLink(link: String) {
    val uri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(this.packageManager) != null) this.startActivity(intent)
    else this.startActivity(intent)
}