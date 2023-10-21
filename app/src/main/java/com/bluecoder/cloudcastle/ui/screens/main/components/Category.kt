package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.ui.theme.BlackHalf

@Composable
fun Category(
    iconId: Int,
    category: String,
    subTitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier
                    .width(38.dp)
                    .height(38.dp)
            )
            Title(
                category,
                subTitle
            )
        }
        Button(
            onClick = {
                onClick()
            },
            Modifier.width(85.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlackHalf)
        ) {
            Text(
                text = stringResource(R.string.see_all_btn),
                color = Black,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun Title(
    title : String,
    subTitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Black
        )
        Text(
            text = subTitle,
            fontWeight = FontWeight.Thin,
            color = Black,
            fontSize = 11.sp
        )
    }
}

@Preview
@Composable
fun Preview() {
    Category(
        R.drawable.photos,
        "Photos",
        "305 Photos, 62 Videos"
    ) {
        println("See all")
    }
}