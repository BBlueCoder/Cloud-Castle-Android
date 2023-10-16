package com.bluecoder.cloudcastle.utils

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bluecoder.cloudcastle.R
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object Utils {

    fun getTimeInMillis() : Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.now().toEpochMilli()
        } else {
            System.currentTimeMillis()
        }
    }

    fun getThumbnailForFiles(fileType : String): Int {
        if(fileType.contains("audio"))
            return R.drawable.music

        if(fileType == "application/pdf")
            return R.drawable.ic_document_pdf

        if(fileType.contains("application") && fileType.contains("spreadsheetml.sheet"))
            return R.drawable.ic_documents_xls

        if(fileType.contains("application") && fileType.contains("wordprocessingml.document"))
            return R.drawable.ic_document_doc

        if(fileType.contains("application") && fileType.contains("zip") || fileType.contains("rar"))
            return R.drawable.ic_document_zip

        if(fileType.contains("application") && !fileType.contains("zip") || fileType.contains("rar"))
            return R.drawable.ic_document_rar

        return R.drawable.ic_document
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

    sealed class UIText {
        data class DynamicString(
            val value : String
        ) : UIText()

        class StringResource(
            @StringRes val resId : Int,
            vararg val args : Any
        ) : UIText()

        @Composable
        fun asString(): String {
            return when(this){
                is DynamicString -> value
                is StringResource -> stringResource(id = resId)
            }
        }
    }
}