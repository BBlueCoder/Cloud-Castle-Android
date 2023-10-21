package com.bluecoder.cloudcastle.ui.screens.content.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.content.MenuItems
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.ui.theme.BlackHalf
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun TopBar(
    fileType: String,
    menuItems: List<MenuItems>,
    isSelectionEnabled: Boolean,
    selectedItemsCount: Int,
    iconTopBarClick: () -> Unit
) {

    val density = LocalDensity.current
    val context = LocalContext.current

    var isDropDownMenuVisible by remember {
        mutableStateOf(false)
    }

    var menuOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var topBarHeight by remember {
        mutableStateOf(0.dp)
    }

    var menuWidth by remember {
        mutableStateOf(0.dp)
    }

    Box {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .onSizeChanged {
                    topBarHeight = with(density) { it.height.toDp() }
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Icon(
                    if (isSelectionEnabled) Icons.Default.Close else Icons.Default.ArrowBack,
                    contentDescription = null,
                    Modifier
                        .clickable {
                            iconTopBarClick()
                        }
                        .semantics {
                            testTag = context.resources.getString(R.string.back_icon_btn)
                        }
                )
                if (isSelectionEnabled) {
                    Text(
                        text = stringResource(R.string.selected_items_count, selectedItemsCount),
                        color = Black,
                        fontWeight = FontWeight.Thin
                    )
                } else {
                    Text(
                        text = getScreenTitle(fileType),
                        style = MaterialTheme.typography.titleMedium,
                        color = Black
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_menu_kebab),
                contentDescription = stringResource(R.string.menu),
                tint = Black,
                modifier = Modifier
                    .clickable {
                        isDropDownMenuVisible = true
                    }
                    .onGloballyPositioned {
                        menuOffset = DpOffset(
                            with(density) { it.positionInRoot().x.toDp() },
                            with(density) { it.positionInRoot().y.toDp() })
                    }
            )
        }

        DropdownMenu(
            expanded = isDropDownMenuVisible,
            onDismissRequest = { isDropDownMenuVisible = false },
            offset = menuOffset.copy(
                x = menuOffset.x - menuWidth + 30.dp,
                y = menuOffset.y - topBarHeight
            ),
            modifier = Modifier
                .background(BlackHalf)
                .onSizeChanged {
                    menuWidth = with(density) { it.width.toDp() }
                }
        ) {
            menuItems.forEach {
                val isEnabled =
                    !((it.title != "delete") && isSelectionEnabled)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it.title,
                            color = Black
                        )
                    },
                    onClick =
                    {
                        isDropDownMenuVisible = false
                        it.onClick()
                    },
                    leadingIcon = {
                        Icon(
                            it.icon,
                            contentDescription = null,
                        )
                    },
                    enabled = isEnabled
                )
            }
        }
    }
}

private fun getScreenTitle(fileType: String): String {
    return when {
        fileType.contains("image") -> "Photos"
        fileType.contains("application") -> "Documents"
        fileType.contains("audio") -> "Sounds"
        else -> ""
    }
}