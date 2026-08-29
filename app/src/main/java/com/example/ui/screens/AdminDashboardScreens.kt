package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.AdminViewModel
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CooperativeAdminDashboard(
    viewModel: AdminViewModel,
    onNavigateToWorkers: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val users by viewModel.allUsers.collectAsStateWithLifecycle()
    val bookings by viewModel.allBookings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cooperative Admin") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Operational Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(Modifier.weight(1f), "Workers", "45", Icons.Default.Engineering)
                AdminStatCard(Modifier.weight(1f), "Active", "12", Icons.Default.EventAvailable)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(Modifier.weight(1f), "Revenue", "₹2.4L", Icons.Default.Payments)
                AdminStatCard(Modifier.weight(1f), "Tickets", "8", Icons.Default.ConfirmationNumber)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Management", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                item { AdminActionCard("Worker Verification", Icons.Default.VerifiedUser, onNavigateToWorkers) }
                item { AdminActionCard("Booking Manager", Icons.Default.Assignment, onNavigateToBookings) }
                item { AdminActionCard("Demand Forecast", Icons.Default.TrendingUp, onNavigateToAnalytics) }
                item { AdminActionCard("Support Center", Icons.Default.SupportAgent, onNavigateToSupport) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminDashboard(
    viewModel: AdminViewModel,
    onNavigateToCooperatives: () -> Unit,
    onNavigateToGlobalSettings: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Super Admin") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Platform Summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            AdminStatCard(Modifier.fillMaxWidth(), "Total Cooperatives", "14", Icons.Default.Business)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(Modifier.weight(1f), "Total Users", "12,400", Icons.Default.Group)
                AdminStatCard(Modifier.weight(1f), "Global Revenue", "₹85L", Icons.Default.AccountBalance)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Platform Controls", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            AdminActionCard("Manage Cooperatives", Icons.Default.Storefront, onNavigateToCooperatives)
            Spacer(modifier = Modifier.height(12.dp))
            AdminActionCard("Platform Settings", Icons.Default.Settings, onNavigateToGlobalSettings)
            Spacer(modifier = Modifier.height(12.dp))
            AdminActionCard("AI Configuration", Icons.Default.Memory, {})
        }
    }
}

@Composable
fun AdminStatCard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector) {
    CodeVedaCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium)
                Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}
