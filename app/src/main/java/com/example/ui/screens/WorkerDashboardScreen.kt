package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDashboardScreen(
    onAiAssistantClick: () -> Unit,
    onWelfareClick: () -> Unit,
    onEarningsClick: () -> Unit
) {
    var isOnline by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAiAssistantClick) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant", tint = MaterialTheme.colorScheme.primary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text(if (isOnline) "Online" else "Offline", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = isOnline, onCheckedChange = { isOnline = it })
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            VerificationBanner()
            Spacer(modifier = Modifier.height(16.dp))
            EarningsCard(onClick = onEarningsClick)
            Spacer(modifier = Modifier.height(24.dp))
            
            CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Worker Welfare Module", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Insurance, Benefits & Support", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(onClick = onWelfareClick) {
                        Text("View")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Upcoming Jobs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text("No upcoming jobs yet.", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Quick Stats", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(Modifier.weight(1f), "Rating", "0.0", Icons.Default.Star)
                StatCard(Modifier.weight(1f), "Jobs", "0", Icons.Default.DoneAll)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(Modifier.weight(1f), "Balance", "₹0", Icons.Default.AccountBalanceWallet)
                StatCard(Modifier.weight(1f), "Points", "0", Icons.Default.Insights)
            }
        }
    }
}

@Composable
fun VerificationBanner() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Verified Professional", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("Your profile is fully verified. You are eligible for emergency jobs.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun EarningsCard(onClick: () -> Unit) {
    CodeVedaCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Weekly Earnings", style = MaterialTheme.typography.titleMedium)
            Text("₹0", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Register services to start earning", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ActiveJobCard(title: String, distance: String, amount: String) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(distance, style = MaterialTheme.typography.bodySmall)
            }
            Text(amount, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {}) {
                Text("Accept")
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    CodeVedaCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
