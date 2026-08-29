package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.ui.AdminViewModel
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerManagementScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val users by viewModel.allUsers.collectAsStateWithLifecycle()
    val workers = users.filter { it.roles.contains(UserRole.WORKER) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(workers) { worker ->
                WorkerManagementItem(
                    worker = worker,
                    onVerify = { viewModel.verifyWorker(worker.id) },
                    onReject = { viewModel.rejectWorker(worker.id) }
                )
            }
        }
    }
}

@Composable
fun WorkerManagementItem(worker: User, onVerify: () -> Unit, onReject: () -> Unit) {
    CodeVedaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(worker.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(worker.email, style = MaterialTheme.typography.bodySmall)
                }
                StatusBadge(worker.verificationStatus)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (worker.verificationStatus == VerificationStatus.PENDING) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onVerify, modifier = Modifier.weight(1f)) { Text("Verify") }
                    OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f)) { Text("Reject") }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: VerificationStatus) {
    val color = when (status) {
        VerificationStatus.VERIFIED -> MaterialTheme.colorScheme.primary
        VerificationStatus.PENDING -> MaterialTheme.colorScheme.secondary
        VerificationStatus.REJECTED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingManagementScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val bookings by viewModel.allBookings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bookings) { booking ->
                BookingManagementItem(booking)
            }
        }
    }
}

@Composable
fun BookingManagementItem(booking: Booking) {
    CodeVedaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Booking #${booking.id.take(8)}", style = MaterialTheme.typography.labelMedium)
                    Text(booking.description ?: "No description", style = MaterialTheme.typography.bodyMedium)
                }
                Text("₹${booking.totalAmount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Status: ${booking.status.name}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
