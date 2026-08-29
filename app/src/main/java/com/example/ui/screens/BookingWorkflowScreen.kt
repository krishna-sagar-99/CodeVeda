package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.WorkerMatch
import com.example.ui.CustomerViewModel
import com.example.ui.components.CodeVedaButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingWorkflowScreen(
    skillId: String,
    viewModel: CustomerViewModel,
    onBookingConfirmed: (String) -> Unit,
    onCancel: () -> Unit
) {
    LaunchedEffect(skillId) {
        viewModel.findWorkers(skillId, 0.0, 0.0) // Mock location
    }

    val matches by viewModel.workerMatches.collectAsStateWithLifecycle()

    var selectedWorker by remember { mutableStateOf<WorkerMatch?>(null) }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedWorker == null) "Select Worker" else "Confirm Booking") },
                navigationIcon = {
                    TextButton(onClick = onCancel) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (matches.isEmpty()) {
                 Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No workers available for this service right now.")
                }
            } else if (selectedWorker == null) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text("Top Matches for you", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "AI Insight: High demand for Electricians in your area. Booking now ensures faster arrival.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items(matches) { match ->
                        WorkerMatchCard(match, onSelect = { selectedWorker = match })
                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                    Text("Service Details", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Describe your problem") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Pricing Summary", style = MaterialTheme.typography.titleMedium)
                    PriceRow("Base Charge", "₹${selectedWorker?.estimatedPrice}")
                    PriceRow("Platform Fee", "₹${(selectedWorker?.estimatedPrice ?: 0.0) * 0.1}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    PriceRow("Total Amount", "₹${(selectedWorker?.estimatedPrice ?: 0.0) * 1.1}", isTotal = true)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    CodeVedaButton(
                        text = "Confirm Booking",
                        onClick = { onBookingConfirmed("b1") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun WorkerMatchCard(match: WorkerMatch, onSelect: () -> Unit) {
    Card(onClick = onSelect, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(match.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (match.matchScore > 90) {
                        Surface(color = Color(0xFFE8F5E9), shape = MaterialTheme.shapes.extraSmall) {
                            Text("Best Match", modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32))
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text("${match.rating} • ${match.distanceKm} km away", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("₹${match.estimatedPrice}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun PriceRow(label: String, value: String, isTotal: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
    }
}
