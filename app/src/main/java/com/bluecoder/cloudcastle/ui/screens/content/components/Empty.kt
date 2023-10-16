package com.bluecoder.cloudcastle.ui.screens.content.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.theme.Black

@Composable
fun Empty() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.empty_folder),
            contentDescription = "error",
            alignment = Alignment.Center,
            modifier = Modifier
                .height(66.dp)
                .width(68.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "It seems that this folder is empty, \n Add new files to it!",
            fontWeight = FontWeight.Normal,
            color = Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Failed() {
    Image(
        painter = painterResource(R.drawable.error),
        contentDescription = "error",
        alignment = Alignment.Center,
        modifier = Modifier
            .height(66.dp)
            .width(68.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "An Error occurred, please try again",
        fontWeight = FontWeight.Normal,
        color = Black,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}