package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.WorkerProfile
import com.example.ui.components.CodeVedaButton
import com.example.ui.components.CodeVedaCard
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerRegistrationScreen(
    userId: String,
    onRegistrationSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var bio by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("10") }
    var emergencyAvailable by remember { mutableStateOf(true) }
    var emergencyContact by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Registration - Step $step/3") },
                navigationIcon = {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (step) {
                1 -> PersonalInfoStep(
                    bio = bio, onBioChange = { bio = it },
                    experience = experience, onExpChange = { experience = it },
                    onNext = { step = 2 }
                )
                2 -> ServiceInfoStep(
                    radius = radius, onRadiusChange = { radius = it },
                    emergencyAvailable = emergencyAvailable, onEmergencyToggle = { emergencyAvailable = it },
                    emergencyContact = emergencyContact, onEmergencyContactChange = { emergencyContact = it },
                    onNext = { step = 3 },
                    onBack = { step = 1 }
                )
                3 -> VerificationStep(
                    onComplete = {
                        // In real app, call repository to save
                        onRegistrationSuccess()
                    },
                    onBack = { step = 2 }
                )
            }
        }
    }
}

@Composable
fun PersonalInfoStep(
    bio: String, onBioChange: (String) -> Unit,
    experience: String, onExpChange: (String) -> Unit,
    onNext: () -> Unit
) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tell us about yourself", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = onBioChange,
                label = { Text("Short Bio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = experience,
                onValueChange = onExpChange,
                label = { Text("Years of Experience") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            CodeVedaButton(text = "Next", onClick = onNext)
        }
    }
}

@Composable
fun ServiceInfoStep(
    radius: String, onRadiusChange: (String) -> Unit,
    emergencyAvailable: Boolean, onEmergencyToggle: (Boolean) -> Unit,
    emergencyContact: String, onEmergencyContactChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Service Details", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = radius,
                onValueChange = onRadiusChange,
                label = { Text("Service Radius (km)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = emergencyAvailable, onCheckedChange = onEmergencyToggle)
                Text("Available for Emergencies")
            }
            if (emergencyAvailable) {
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = onEmergencyContactChange,
                    label = { Text("Emergency Contact Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
                CodeVedaButton(text = "Next", onClick = onNext, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun VerificationStep(onComplete: () -> Unit, onBack: () -> Unit) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Identity Verification", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Please upload a clear photo of your Government Issued ID (Aadhar/PAN).", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { /* Upload Simulation */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Upload ID Photo")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
                CodeVedaButton(text = "Submit for Review", onClick = onComplete, modifier = Modifier.weight(1f))
            }
        }
    }
}
