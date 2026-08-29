package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.BookingStatus
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingTrackingScreen(
    bookingId: String,
    onNavigateBack: () -> Unit,
    onChatOpen: () -> Unit
) {
    var status by remember { mutableStateOf(BookingStatus.ON_THE_WAY) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tracking Booking") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Simulated Map Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                    Text("Live Map View", style = MaterialTheme.typography.labelLarge)
                    Text("Worker is 1.2 km away", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Booking Details Bottom Sheet style
            Surface(
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Gray))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Amit Kumar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Verified Electrician", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2E7D32))
                        }
                        IconButton(onClick = { /* Call Simulation */ }) { Icon(Icons.Default.Call, contentDescription = "Call") }
                        IconButton(onClick = onChatOpen) { Icon(Icons.Default.Chat, contentDescription = "Chat") }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Status: ${status.name.replace("_", " ")}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = 0.6f)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Close Tracking")
                    }
                }
            }
        }
    }
}
