package com.bluecoder.cloudcastle.ui.screens.content.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.ui.theme.BlackHalf
import com.bluecoder.cloudcastle.ui.theme.SmokeWhite

@Composable
fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismissClick()
        },
        icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Black) },
        title = {
            Text(text = "Permission required", color = Black)
        },
        text = {
            Text(text = PostNotificationPermissionText().getText(isPermanentlyDeclined), color = Black)
        },
        confirmButton = {
            TextButton(onClick = { onConfirmClick() }) {
                Text(text = "OK", color = Black)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissClick()
            }) {
                Text(text = "Dismiss", color = Color.Red)
            }
        },
        containerColor = Color.White
    )
}

interface PermissionText {
    fun getText(isPermanentlyDeclined : Boolean): String
}

class PostNotificationPermissionText: PermissionText {
    override fun getText(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "You can't receive notifications unless you give the permission in the app's settings"
        }else {
            "To receive notifications click on allow"
        }
    }

}