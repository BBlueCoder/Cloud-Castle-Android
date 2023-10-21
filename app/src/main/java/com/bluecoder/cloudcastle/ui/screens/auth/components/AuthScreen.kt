package com.bluecoder.cloudcastle.ui.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.ui.theme.Orange
import com.bluecoder.cloudcastle.ui.theme.StrongWhite
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun AuthScreen(
    screenTitle : String,
    contentPadding: PaddingValues,
    usernameValue: String,
    usernameErrorState: MutableState<Utils.UIText>,
    updateUsername: (username: String) -> Unit,
    passwordValue: String,
    passwordErrorState: MutableState<Utils.UIText>,
    updatePassword: (username: String) -> Unit,
    secondaryBtn: () -> Unit,
    secondaryText: String,
    mainBtn: () -> Unit,
    mainText: String
) {
    Box(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {

            Text(
                text = screenTitle,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlineTextField(
                value = usernameValue,
                label = stringResource(id = R.string.enter_username),
                testTag = stringResource(id = R.string.username),
                isError = usernameErrorState.value.asString().isNotEmpty(),
                errorMessage = usernameErrorState.value.asString()
            ) {
                updateUsername(it)
            }

            OutlineTextField(
                value = passwordValue,
                label = stringResource(id = R.string.enter_password),
                testTag = stringResource(id = R.string.password),
                transformation = PasswordVisualTransformation(),
                isError = passwordErrorState.value.asString().isNotEmpty(),
                errorMessage = passwordErrorState.value.asString()
            ) {
                updatePassword(it)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedButton(
                    onClick = {
                        secondaryBtn()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = secondaryText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                ElevatedButton(
                    onClick = {
                        mainBtn()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        mainText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun OutlineTextField(
    value: String,
    label: String,
    testTag: String,
    isError: Boolean,
    errorMessage: String,
    transformation: PasswordVisualTransformation? = null,
    onValueChange: (value: String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Orange,
            unfocusedTextColor = Black,
            focusedContainerColor = StrongWhite,
            unfocusedContainerColor = StrongWhite,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        visualTransformation = transformation ?: VisualTransformation.None,
        singleLine = true,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}