package com.bluecoder.cloudcastle.ui.screens.display

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bluecoder.cloudcastle.ui.screens.display.ui.theme.CloudCastleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val intent = intent
        val fileId = intent.getIntExtra("file_id",0)
        setContent {
            CloudCastleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayScreen(fileId = fileId, onBackPressed = {finish()})
                }
            }
        }
    }
}