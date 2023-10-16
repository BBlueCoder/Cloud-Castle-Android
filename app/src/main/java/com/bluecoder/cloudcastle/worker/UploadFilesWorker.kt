package com.bluecoder.cloudcastle.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.ui.screens.content.hasPermission
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@HiltWorker
class UploadFilesWorker @AssistedInject constructor(
    private val repo: FilesRepo,
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORKER_TAG = "UPLOAD_FILES_WORKER_TAG"
        const val FILES_URIS_KEY = "FILES_URIS"
    }

    override suspend fun doWork(): Result {
        val contentResolver = context.contentResolver
        val uris = inputData.getStringArray(FILES_URIS_KEY)?.toList()

        return withContext(Dispatchers.IO) {
            try {
                if (hasPermission(Manifest.permission.POST_NOTIFICATIONS, context))
                    setForeground(createForegroundInfo())

                uris?.let {
                    uploadFiles(contentResolver, it)
                }
                Result.success()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Result.failure()
            }
        }
    }

    private suspend fun uploadFiles(contentResolver: ContentResolver, uris: List<String>) {
        val multiPartList = mutableListOf<MultipartBody.Part>()

        uris.forEach { uriString ->
            contentResolver.openFileDescriptor(
                uriString.toUri(),
                "r",
                null
            ).use { fileDescriptor ->
                fileDescriptor?.let { fileDescriptorNotNull ->
                    FileInputStream(fileDescriptorNotNull.fileDescriptor).use { inputStream ->
                        val file =
                            File(context.cacheDir, contentResolver.getFileName(uriString.toUri()))

                        val outputStream = FileOutputStream(file)
                        inputStream.copyTo(outputStream)

                        val bodyPart = MultipartBody.Part.createFormData(
                            "files",
                            file.name,
                            file
                                .asRequestBody(
                                    contentResolver.getType(uriString.toUri())?.toMediaTypeOrNull()
                                )
                        )

                        multiPartList.add(bodyPart)
                    }
                }
            }
        }

        if (multiPartList.isNotEmpty()) {
            val resp = repo.addFiles(multiPartList)

            if (resp.isFailure)
                throw Exception(resp.getOrThrow())
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(context, "Channel_ID")
            .setContentTitle("Upload files to server")
            .setTicker("Uploading files...")
            .setSmallIcon(R.drawable.ic_cloud)
            .setOngoing(true)
            .setProgress(100, 0, true)
            .build()

        return ForegroundInfo(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "Channel_ID",
            "Uploading files",
            NotificationManager.IMPORTANCE_HIGH
        ).let {
            it.description = "Downloading file and saving it"
            it.enableLights(true)
            it.lightColor = Color.RED
            it.enableVibration(true)
            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            it
        }
        notificationManager.createNotificationChannel(channel)
    }
}


fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val returnCursor = this.query(uri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)

        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }

    return name
}