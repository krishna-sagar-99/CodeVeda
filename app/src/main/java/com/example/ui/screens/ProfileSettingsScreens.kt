package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.UserRole
import com.example.ui.components.CodeVedaCard

@Composable
fun ProfileScreen(
    currentRole: UserRole,
    onSwitchRole: (UserRole) -> Unit,
    onLogout: () -> Unit,
    onHistoryClick: () -> Unit,
    onRegisterWorker: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            modifier = Modifier.size(100.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("John Doe", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("john.doe@example.com", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        ProfileOption(Icons.Default.AccountCircle, "Account Details", onClick = {})
        ProfileOption(Icons.Default.History, "Booking History", onClick = onHistoryClick)
        ProfileOption(Icons.Default.AdminPanelSettings, "Cooperative Admin", onClick = onNavigateToAdmin)
        ProfileOption(Icons.Default.Payment, "Payment Methods", onClick = {})

        Spacer(modifier = Modifier.height(24.dp))

        CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Professional Mode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                if (currentRole == UserRole.CUSTOMER) {
                    Text("Want to earn by providing services?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRegisterWorker, modifier = Modifier.fillMaxWidth()) {
                        Text("Register as Worker")
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Worker Mode Active")
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { onSwitchRole(UserRole.CUSTOMER) }) {
                            Text("Switch to Customer")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun ProfileOption(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@Composable
fun SettingsScreen(
    isDark: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DarkMode, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Dark Mode")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isDark, onCheckedChange = onThemeToggle)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Language, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Language")
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { onLanguageChange(if (language == "English") "Hindi" else "English") }) {
                Text(language)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        Text("About CodeVeda", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("CodeVeda is a community-focused gig services platform empowering cooperative workers and providing trusted services to households.")
    }
}
