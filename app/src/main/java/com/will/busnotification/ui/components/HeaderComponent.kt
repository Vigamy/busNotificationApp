package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HeaderComponent(
    modifier: Modifier = Modifier,
    text: String,
    hasBack: Boolean,
    navController: NavController? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF5B8FCF))
            .padding(vertical = 32.dp)
    ) {
        if (hasBack) {
            IconButton(onClick = { navController?.popBackStack()} ) {
                Icon(
                    modifier = modifier.size(64.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        }
        Text(
            modifier = modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = if (hasBack) 48.dp else 32.dp),
            text = text,
            color = Color.White,
            fontSize = 28.sp,
        )
        if (hasBack) {
            IconButton(
                modifier = modifier
                    .align(Alignment.CenterEnd),
                onClick = { navController?.navigate("notifications") },
            ) {
                Icon(
                    modifier = modifier
                        .size(64.dp)
                        .padding(end = 40.dp),
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderComponentPreview() {
    HeaderComponent(text = "Adicionar notificacao", hasBack = true)
}