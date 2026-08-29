package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CustomerViewModel
import com.example.ui.components.CodeVedaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    viewModel: CustomerViewModel,
    onCategoryClick: (String) -> Unit,
    onEmergencyClick: () -> Unit,
    onAiAssistantClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CodeVeda", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAiAssistantClick) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
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
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("What service do you need?") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.large
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                    Text("Loading categories...", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(categories) { category ->
                        ServiceCategoryItem(
                            name = category.name,
                            iconName = category.iconName,
                            onClick = { onCategoryClick(category.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            QuickActionsSection(onEmergencyClick)
        }
    }
}

@Composable
fun AIRecommendationsSection() {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CodeVeda AI Recommendations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Based on your profile, we recommend booking an Electrician for annual maintenance and checking out our new Gardening services.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ServiceCategoryItem(name: String, iconName: String, onClick: () -> Unit) {
    val icon = when (iconName) {
        "FlashOn" -> Icons.Default.FlashOn
        "WaterDrop" -> Icons.Default.WaterDrop
        "Construction" -> Icons.Default.Construction
        "FormatPaint" -> Icons.Default.FormatPaint
        "CleaningServices" -> Icons.Default.CleaningServices
        "Park" -> Icons.Default.Park
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "Handyman" -> Icons.Default.Handyman
        else -> Icons.Default.Handyman
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        CodeVedaCard(modifier = Modifier.size(70.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun QuickActionsSection(onEmergencyClick: () -> Unit) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickActionItem(Icons.Default.FlashOn, "Emergency", onClick = onEmergencyClick)
            QuickActionItem(Icons.Default.Schedule, "Schedule", onClick = {})
            QuickActionItem(Icons.Default.History, "Recent", onClick = {})
            QuickActionItem(Icons.Default.Star, "Favs", onClick = {})
        }
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

data class Category(val name: String, val icon: ImageVector)

val serviceCategories = listOf(
    Category("Electrician", Icons.Default.FlashOn),
    Category("Plumber", Icons.Default.WaterDrop),
    Category("Carpenter", Icons.Default.Construction),
    Category("Painter", Icons.Default.FormatPaint),
    Category("Cleaner", Icons.Default.CleaningServices),
    Category("Gardener", Icons.Default.Park),
    Category("Driver", Icons.Default.DirectionsCar),
    Category("Repair", Icons.Default.Handyman)
)
