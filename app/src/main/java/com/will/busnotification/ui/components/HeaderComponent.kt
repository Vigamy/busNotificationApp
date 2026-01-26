package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderComponent(
    modifier: Modifier = Modifier,
    text: String,
    hasBack: Boolean,
    hasBell: Boolean = false,
    titleCentered: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onBellClick: (() -> Unit)? = null
) {
    // Standard icon container width to avoid title overlap
    val iconSlotWidth: Dp = 56.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF5B8FCF))
            .padding(vertical = 16.dp)
    ) {
        // Back button (if requested)
        if (hasBack) {
            IconButton(
                onClick = { onBackClick?.invoke() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Title - either centered or start-aligned (preserve previous behavior by default)
        if (titleCentered) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                color = Color.White,
                fontSize = 20.sp,
            )
        } else {
            // Align start but keep a left inset so it doesn't overlap with the back icon
            val startPadding = if (hasBack) iconSlotWidth else 16.dp
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = startPadding),
                text = text,
                color = Color.White,
                fontSize = 20.sp,
            )
        }

        // Bell (notifications) button - only if hasBell is true
        if (hasBell) {
            IconButton(
                onClick = { onBellClick?.invoke() },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderComponentPreview() {
    HeaderComponent(text = "Adicionar notificacao", hasBack = false, hasBell = true)
}