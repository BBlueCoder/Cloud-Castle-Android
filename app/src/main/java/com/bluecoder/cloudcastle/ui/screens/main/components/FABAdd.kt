package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.ui.theme.CloudCastleTheme

@Composable
fun FABAdd() {
    ExtendedFloatingActionButton(
        onClick = { },
        text = {
            Text(text = "Add")
        },
        icon = {
            Icon(
                Icons.Default.Add,
                "Add icon"
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(15.dp)
    )
}

@Preview
@Composable
fun PreviewFAB() {
    CloudCastleTheme {
        FABAdd()
    }
}