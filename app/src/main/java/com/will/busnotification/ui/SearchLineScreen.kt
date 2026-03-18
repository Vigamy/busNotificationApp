package com.will.busnotification.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.data.model.TransitSegment
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.viewmodel.SearchLineViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private val BluePrimary = Color(0xFF5B8FCF)
private val BlueLight = Color(0xFFE0EAFC)

@Composable
fun SearchLineScreen(
    navController: NavHostController,
    viewModel: SearchLineViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val emptyResults by viewModel.emptyResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
    ) {
        HeaderComponent(
            text = "Adicionar Notificação",
            hasBack = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // --- Search field ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Digite o destino (ex: Terminal Lapa)",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = BluePrimary
                    )
                },
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = BluePrimary
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    unfocusedBorderColor = Color(0xFFD0D5DD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // --- Hint text ---
            AnimatedVisibility(
                visible = searchQuery.isBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Busque pelo nome do destino para encontrar linhas de ônibus disponíveis",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Content area ---
            when {
                // Loading state (only when no results yet)
                isSearching && searchResults.isEmpty() -> {
                    LoadingState()
                }

                // Error state
                errorMessage != null -> {
                    StatusMessage(
                        icon = Icons.Outlined.ErrorOutline,
                        title = "Ops!",
                        message = errorMessage!!,
                        iconTint = Color(0xFFD32F2F)
                    )
                }

                // Empty results after search
                emptyResults -> {
                    StatusMessage(
                        icon = Icons.Outlined.SearchOff,
                        title = "Nenhuma rota encontrada",
                        message = "Tente um destino diferente ou mais específico. Ex: \"Terminal Lapa\" ou \"Shopping Eldorado\"",
                        iconTint = Color(0xFFFF9800)
                    )
                }

                // Search results
                searchResults.isNotEmpty() -> {
                    Text(
                        text = "${searchResults.size} linha${if (searchResults.size > 1) "s" else ""} encontrada${if (searchResults.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(searchResults) { segment ->
                            BusResultCard(
                                segment = segment,
                                onClick = {
                                    val enc = { s: String ->
                                        URLEncoder.encode(s, StandardCharsets.UTF_8.toString())
                                    }
                                    navController.navigate(
                                        "notificationSetup/${segment.lineCode}/${enc(segment.lineName)}/${enc(segment.departureStop)}/${enc(segment.arrivalStop)}/${segment.arrivalTime}/${segment.headsign}"
                                    )
                                }
                            )
                        }
                    }
                }

                // Initial state — nothing searched yet
                searchQuery.isBlank() -> {
                    StatusMessage(
                        icon = Icons.Outlined.DirectionsBus,
                        title = "Para onde você vai?",
                        message = "Pesquise seu destino para ver as linhas de ônibus que passam perto de você",
                        iconTint = BluePrimary
                    )
                }
            }
        }
    }
}

// ─── Components ─────────────────────────────────────────────────────────

@Composable
private fun BusResultCard(
    segment: TransitSegment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Line code badge
            Box(
                modifier = Modifier
                    .background(BlueLight, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = segment.lineCode,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BluePrimary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Route info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = segment.lineName.split("/").first().trim(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1A1A2E)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${segment.departureStop} → ${segment.arrivalStop}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }

            // Departure time
            if (segment.departureTime.isNotBlank()) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatTime(segment.departureTime),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BluePrimary
                    )
                    Text(
                        text = "partida",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = BluePrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Buscando rotas...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StatusMessage(
    icon: ImageVector,
    title: String,
    message: String,
    iconTint: Color = BluePrimary
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A2E)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

/**
 * Extract a human-readable time from ISO or time string.
 * Handles "2026-03-18T14:30:00-03:00" → "14:30"
 * or already short strings like "14:30".
 */
private fun formatTime(raw: String): String {
    // ISO format: try to extract HH:mm from "...THH:mm:ss..."
    val tIndex = raw.indexOf('T')
    if (tIndex != -1 && raw.length >= tIndex + 6) {
        return raw.substring(tIndex + 1, tIndex + 6)
    }
    // Already short enough
    return if (raw.length >= 5) raw.substring(0, 5) else raw
}

@Preview(showBackground = true)
@Composable
fun SearchLineScreenPreview() {
    SearchLineScreen(navController = rememberNavController())
}
