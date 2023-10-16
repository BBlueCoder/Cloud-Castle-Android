package com.bluecoder.cloudcastle.ui.screens.content

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bluecoder.cloudcastle.worker.UploadFilesWorker
import com.bluecoder.cloudcastle.ui.screens.content.components.Empty
import com.bluecoder.cloudcastle.ui.screens.content.components.Failed
import com.bluecoder.cloudcastle.ui.screens.content.components.ListDocuments
import com.bluecoder.cloudcastle.ui.screens.content.components.ListPhotos
import com.bluecoder.cloudcastle.ui.screens.content.components.PermissionDialog
import com.bluecoder.cloudcastle.ui.screens.content.components.TopBar
import com.bluecoder.cloudcastle.ui.screens.content.components.findActivity
import com.bluecoder.cloudcastle.ui.screens.content.components.openAppSettings
import com.bluecoder.cloudcastle.ui.screens.display.DisplayActivity
import kotlinx.coroutines.flow.asStateFlow

data class MenuItems(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun ContentScreen(
    navController: NavController,
    fileType: String,
    viewModel: ContentScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val workManager = WorkManager.getInstance(context)


    val contentsUiState by viewModel.contentsState.collectAsStateWithLifecycle()

    val isContentsLoading = contentsUiState == ContentsUiState.Loading
    val isContentsLoadingFailed = contentsUiState == ContentsUiState.Error


    var isSelectionEnabled by remember {
        mutableStateOf(false)
    }

    val selectedItemsCount by viewModel.selectedItemsCount.asStateFlow()
        .collectAsStateWithLifecycle()

    var urisList by remember {
        mutableStateOf(emptyList<Uri>())
    }

    var declinedPermission by remember {
        mutableStateOf("")
    }

    observeWorkManager(lifecycleOwner,viewModel,workManager)

    val postNotificationResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                declinedPermission = Manifest.permission.POST_NOTIFICATIONS
            }
        } else {
            startUploading(urisList, workManager, context) {}
        }
    }

    when(declinedPermission){
        Manifest.permission.POST_NOTIFICATIONS -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    context.findActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
                PermissionDialog(
                    isPermanentlyDeclined = isPermanentlyDeclined,
                    onConfirmClick = {
                        if(isPermanentlyDeclined){
                            context.findActivity().openAppSettings()
                        }else{
                            postNotificationResultLauncher.launch(declinedPermission)
                        }
                        declinedPermission = ""
                    },
                    onDismissClick = {
                        declinedPermission = ""
                    }
                )
            }
        }
    }

    val pickMultipleMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                urisList = uris
                startUploading(urisList, workManager,context) {
                    postNotificationResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        }
    )

    val pickDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data

                data?.let {
                    startUploading(handleActivityResult(it),workManager,context){
                        postNotificationResultLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }
            }
        }
    )

    val menuItems = listOf(
        MenuItems("select", Icons.Default.Edit) {
            isSelectionEnabled = true
        },
        MenuItems("import", Icons.Default.Add) {
            when{
                fileType.contains("application") -> {
                    pickDocumentLauncher.launch(
                        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "*/*"
                            val mimeTypes = arrayOf(
                                "application/*",
                                "text/*"
                            )
                            putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                        }
                    )
                }
                fileType.contains("image") -> {
                    pickMultipleMedia.launch(PickVisualMediaRequest())
                }
                fileType.contains("audio") -> {
                    pickDocumentLauncher.launch(
                        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "audio/*"

                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                        }
                    )
                }
            }
        },
        MenuItems("delete", Icons.Default.Delete) {
            viewModel.deleteFiles()
            isSelectionEnabled = false
        },
        MenuItems("filter", Icons.Default.Menu) {},
    )


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
    ) {
        TopBar(
            fileType,
            menuItems,
            isSelectionEnabled,
            selectedItemsCount
        ) {
            if (isSelectionEnabled) {
                isSelectionEnabled = false
                viewModel.unselectAllItems()
            } else {
                navController.popBackStack()
            }
        }

        if (!isContentsLoading && !isContentsLoadingFailed) {
            val data = (contentsUiState as ContentsUiState.Success).content

            if (data.isEmpty()) {
                Empty()
            } else {
                Spacer(modifier = Modifier.height(40.dp))

                if (fileType.contains("image") || fileType.contains("video")) {
                    ListPhotos(data = data, isSelectionEnabled = isSelectionEnabled) { fileItem ->
                        if (isSelectionEnabled) {
                            viewModel.toggleSelection(fileItem)
                        } else{
                            val intent = Intent(context,DisplayActivity::class.java).apply {
                                putExtra("file_id",fileItem.id)
                            }
                            context.startActivity(intent)
                        }
                    }
                }

                if (fileType.contains("application") || fileType.contains("audio")) {
                    ListDocuments(data = data, isSelectionEnabled = isSelectionEnabled){fileItem ->
                        if (isSelectionEnabled) {
                            viewModel.toggleSelection(fileItem)
                        }else{
                            if(fileType.contains("audio")){
                                val intent = Intent(context,DisplayActivity::class.java).apply {
                                    putExtra("file_id",fileItem.id)
                                }
                                context.startActivity(intent)
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(40.dp))
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isContentsLoading) {
                    CircularProgressIndicator()
                }
                if (isContentsLoadingFailed) {
                    Failed()
                }
            }
        }
    }
}

fun hasPermission(permission : String, context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED
}

private fun startUploading(
    uris : List<Uri>,
    workManager: WorkManager,
    context: Context,
    askForPermission : () -> Unit
    ){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if(!hasPermission(Manifest.permission.POST_NOTIFICATIONS,context)){
            askForPermission()
        }
    }

    val urisArray : Array<String> = uris.map { it.toString() }.toTypedArray()
    val request = OneTimeWorkRequestBuilder<UploadFilesWorker>()
        .setInputData(
            workDataOf(UploadFilesWorker.FILES_URIS_KEY to urisArray)
        )
        .addTag(UploadFilesWorker.WORKER_TAG)
        .build()
    workManager.enqueue(request)
}

private fun handleActivityResult(intentResult : Intent): List<Uri> {
    val uris = mutableListOf<Uri>()

    intentResult.data?.let {uri ->
        uris.add(uri)
    }
    intentResult.clipData?.let {clipData ->
        for (i in 0 until clipData.itemCount){
            clipData.getItemAt(i)?.let { uri -> uris.add(uri.uri) }
        }
    }
    return uris
}

private fun observeWorkManager(
    lifecycleOwner : LifecycleOwner,
    viewModel : ContentScreenViewModel,
    workManager : WorkManager
) {
    workManager.getWorkInfosByTagLiveData(UploadFilesWorker.WORKER_TAG)
        .observe(lifecycleOwner) { workInfo ->
            workInfo.forEach {
                if(it != null && it.state == WorkInfo.State.SUCCEEDED){
                    viewModel.fetchFiles()
                }
            }
        }
}