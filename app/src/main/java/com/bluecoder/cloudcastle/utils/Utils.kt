package com.bluecoder.cloudcastle.utils

import com.bluecoder.cloudcastle.R
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.DurationUnit

object Utils {

    fun getThumbnailForFiles(fileType : String): Int {
        if(fileType.contains("audio"))
            return R.drawable.music

        if(fileType == "application/pdf")
            return R.drawable.pdf

        if(fileType.contains("application") && fileType.contains("spreadsheetml.sheet"))
            return R.drawable.xls

        if(fileType.contains("application") && fileType.contains("wordprocessingml.document"))
            return R.drawable.doc

        if(fileType.contains("application") && fileType.contains("zip") || fileType.contains("rar"))
            return R.drawable.zip

        return R.drawable.document
    }

    fun formatDuration(duration : Double) : String{
        if(duration < 1)
            return ""

        val seconds = duration.toInt().toLong()
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val remainingSeconds = duration.toInt() % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        } else {
            String.format("%02d:%02d", minutes, remainingSeconds)
        }
    }

    fun shortenFilenameIdNeeded(filename : String): String {
        return if (filename.length > 20) {
            val firstNineChars = filename.substring(0, 13)
            val lastChars = filename.substring(filename.length - (20 - 13))
            "$firstNineChars...$lastChars"
        } else {
            filename
        }
    }
}