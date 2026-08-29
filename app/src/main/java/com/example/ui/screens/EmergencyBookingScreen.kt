package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CodeVedaButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyBookingScreen(onFindEmergencyWorker: (String) -> Unit, onCancel: () -> Unit) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Electrical Short Circuit", "Major Water Leak", "Gas Leak", "Medical Assistance", "Lockout")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Service") },
                navigationIcon = {
                    TextButton(onClick = onCancel) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Emergency Assistance Required?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("We will connect you to the nearest available verified worker in minutes.", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Select Emergency Category", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))
            
            categories.forEach { category ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedCategory == category, onClick = { selectedCategory = category })
                    Text(category, modifier = Modifier.padding(start = 8.dp))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            CodeVedaButton(
                text = "Find Nearest Worker",
                onClick = { selectedCategory?.let { onFindEmergencyWorker(it) } },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Red,
                contentColor = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("A higher priority fee may apply for emergency services.", style = MaterialTheme.typography.labelSmall)
        }
    }
}
