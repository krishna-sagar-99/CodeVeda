package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsDashboardScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earnings & Payouts") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                EarningsSummary()
            }
            item {
                CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(androidx.compose.material.icons.Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("AI Fair Wage Insight", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Your average hourly rate is 15% higher than the local average for your skill level. Great work!", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            item {
                Text("Payout History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No payout history yet.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun EarningsSummary() {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Available for Payout", style = MaterialTheme.typography.labelLarge)
            Text("₹2,500.00", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Payout Simulation */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Withdraw to Bank")
            }
        }
    }
}

@Composable
fun PayoutItem() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("24 Aug, 2026", style = MaterialTheme.typography.labelSmall)
                Text("Weekly Payout", style = MaterialTheme.typography.titleSmall)
            }
            Text("₹5,200.00", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        }
    }
}
