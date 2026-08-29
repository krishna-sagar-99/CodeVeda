package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
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
fun PaymentScreen(bookingId: String, amount: Double, onPaymentSuccess: () -> Unit) {
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Secure Payment") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isSuccess) {
                Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Total to Pay", style = MaterialTheme.typography.titleMedium)
                Text("₹$amount", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Payment Method", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentMethodRow("UPI (Google Pay/PhonePe)", true)
                        PaymentMethodRow("Credit/Debit Card", false)
                        PaymentMethodRow("Net Banking", false)
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                CodeVedaButton(
                    text = if (isProcessing) "Processing..." else "Pay Now",
                    onClick = {
                        isProcessing = true
                        // Simulation delay
                        isSuccess = true
                        isProcessing = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(96.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Payment Successful!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Invoice #INV-${bookingId.take(5)} generated.", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(48.dp))
                        CodeVedaButton(text = "Back to Home", onClick = onPaymentSuccess)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodRow(label: String, selected: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = {})
        Text(label, modifier = Modifier.padding(start = 12.dp))
    }
}
