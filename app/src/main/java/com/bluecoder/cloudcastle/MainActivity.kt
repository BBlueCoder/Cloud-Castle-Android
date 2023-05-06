package com.bluecoder.cloudcastle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bluecoder.cloudcastle.ui.screens.CloudCastle
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModel
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModel
import com.bluecoder.cloudcastle.ui.theme.CloudCastleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authViewModel : AuthViewModel = viewModel()
            val mainViewModel : MainViewModel = viewModel()

            CloudCastle(authViewModel = authViewModel, mainViewModel = mainViewModel)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CloudCastleTheme {
        Greeting("Android")
    }
}