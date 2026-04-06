package com.buildcal.probuild.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.InputField
import com.buildcal.probuild.ui.theme.GradientEnd
import com.buildcal.probuild.ui.theme.GradientStart
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val vm: ProfileViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    var name by remember(state.name) { mutableStateOf(state.name) }
    var email by remember(state.email) { mutableStateOf(state.email) }
    var saved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(GradientStart, GradientEnd))).padding(top = 40.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(96.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.name.isNotEmpty()) {
                        Text(state.name.first().uppercase(), style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Filled.Person, null, tint = Color.White, modifier = Modifier.size(56.dp))
                    }
                }
                if (state.name.isNotEmpty()) {
                    Text(state.name, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                }
                if (state.email.isNotEmpty()) {
                    Text(state.email, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Edit Profile", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    InputField(value = name, onValueChange = { name = it; saved = false }, label = "Name")
                    InputField(value = email, onValueChange = { email = it; saved = false }, label = "Email")
                }
            }

            Button(
                onClick = {
                    vm.saveName(name.trim())
                    vm.saveEmail(email.trim())
                    saved = true
                },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null)
                Spacer(Modifier.width(8.dp))
                Text(if (saved) "Saved!" else "Save Profile", modifier = Modifier.padding(vertical = 4.dp))
            }

            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("App Info", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.Calculate, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Column {
                            Text("Build Calc Pro", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("Construction Material Calculator", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Version 1.0.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
