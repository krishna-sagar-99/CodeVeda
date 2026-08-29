package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.CodeVedaButton
import com.example.ui.components.CodeVedaCard

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R

@Composable
fun LandingScreen(
    onBookService: () -> Unit,
    onBecomeWorker: () -> Unit
) {
    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1788004127337),
                    contentDescription = "Hero banner",
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                HeroSection(onBookService, onBecomeWorker)
            }
            item {
                StatsSection()
            }
            item {
                HowItWorksSection()
            }
            item {
                BenefitSection()
            }
        }
    }
}

@Composable
fun HeroSection(onBookService: () -> Unit, onBecomeWorker: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trusted Local Services, Powered by Cooperatives.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "CodeVeda connects verified cooperative workers with households and institutions for reliable, fair-priced community services.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        CodeVedaButton(text = "Book a Service", onClick = onBookService)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBecomeWorker,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Become a Worker")
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("500+", "Verified Workers")
        StatItem("10k+", "Happy Homes")
        StatItem("15+", "Cooperatives")
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun HowItWorksSection() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("How CodeVeda Works", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        HowItWorksItem(1, "Select a Service", "Choose from a wide range of verified household services.")
        HowItWorksItem(2, "AI Matching", "Our AI finds the best cooperative worker near you.")
        HowItWorksItem(3, "Service Delivery", "The worker arrives and provides top-quality service.")
    }
}

@Composable
fun HowItWorksItem(step: Int, title: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(step.toString(), fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(desc, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BenefitSection() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Why Cooperative Workers?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        BenefitItem("Verified Skills", "Every worker is trained and certified by recognized cooperatives.")
        BenefitItem("Fair Pricing", "Transparent pricing models ensure fair wages and affordable services.")
        BenefitItem("Community Trust", "Locally rooted cooperatives ensure accountability and safety.")
    }
}

@Composable
fun BenefitItem(title: String, desc: String) {
    CodeVedaCard(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(desc, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
