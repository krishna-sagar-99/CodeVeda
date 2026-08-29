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
fun SupportCenterScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val tickets by viewModel.allTickets.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Support Center") },
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
            items(tickets) { ticket ->
                SupportTicketItem(ticket, onResolve = { viewModel.resolveTicket(ticket.id) })
            }
        }
    }
}

@Composable
fun SupportTicketItem(ticket: SupportTicket, onResolve: () -> Unit) {
    CodeVedaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(ticket.category.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Text(ticket.subject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                TicketStatusBadge(ticket.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(ticket.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            if (ticket.status == TicketStatus.OPEN || ticket.status == TicketStatus.IN_PROGRESS) {
                Button(onClick = onResolve, modifier = Modifier.fillMaxWidth()) {
                    Text("Mark as Resolved")
                }
            }
        }
    }
}

@Composable
fun TicketStatusBadge(status: TicketStatus) {
    val color = when (status) {
        TicketStatus.OPEN -> MaterialTheme.colorScheme.error
        TicketStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary
        TicketStatus.RESOLVED -> MaterialTheme.colorScheme.primary
        TicketStatus.CLOSED -> MaterialTheme.colorScheme.outline
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
fun WorkerWelfareModule(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Welfare") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Benefit Programs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            WelfareProgramCard("Cooperative Health Insurance", "Enrollment Active", Icons.Default.HealthAndSafety)
            WelfareProgramCard("Accident Coverage", "Eligible - Documents Required", Icons.Default.Shield)
            WelfareProgramCard("Micro-Savings Scheme", "Monthly Contribution Active", Icons.Default.Savings)
            WelfareProgramCard("Skill Certification Fund", "Apply for Reimbursement", Icons.Default.School)

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Request Support")
            }
        }
    }
}

@Composable
fun WelfareProgramCard(title: String, status: String, icon: ImageVector) {
    CodeVedaCard {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(status, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
