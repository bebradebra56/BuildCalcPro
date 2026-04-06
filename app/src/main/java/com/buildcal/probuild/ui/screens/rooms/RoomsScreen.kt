package com.buildcal.probuild.ui.screens.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.db.entity.RoomEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RoomsScreen(navController: NavController, projectId: Long) {
    val vm: RoomsViewModel = koinViewModel(parameters = { parametersOf(projectId) })
    val state by vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.rooms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyStateMessage(
                    icon = Icons.Filled.MeetingRoom,
                    title = "No rooms yet",
                    subtitle = "Tap + to add a room"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.rooms, key = { it.id }) { room ->
                    SwipeToDeleteBox(onDelete = { vm.deleteRoom(room) }) {
                        RoomCard(room = room)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddRoom.createRoute(projectId)) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Room", tint = Color.White)
        }
    }
}

@Composable
private fun RoomCard(room: RoomEntity) {
    val area = room.length * room.width
    val volume = area * room.height
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.MeetingRoom, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(26.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(room.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${"%.2f".format(room.length)}m × ${"%.2f".format(room.width)}m × ${"%.2f".format(room.height)}m", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DimensionChip("Area: ${"%.1f".format(area)} m²")
                    DimensionChip("Vol: ${"%.1f".format(volume)} m³")
                }
            }
        }
    }
}

@Composable
private fun DimensionChip(text: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
