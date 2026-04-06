package com.buildcal.probuild.ui.screens.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.db.entity.ShoppingItemEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.theme.GradientEnd
import com.buildcal.probuild.ui.theme.GradientStart
import org.koin.androidx.compose.koinViewModel

private val unitOptions = listOf("pcs", "m²", "m³", "m", "kg", "L", "roll", "bag", "sheet")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(navController: NavController) {
    val vm: ShoppingViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Shopping List", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    if (state.pendingCount > 0) {
                        Text("${state.pendingCount} items to buy", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            if (state.items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyStateMessage(icon = Icons.Filled.ShoppingCart, title = "Shopping list is empty", subtitle = "Add items you need to buy")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pending = state.items.filter { !it.isPurchased }
                    val purchased = state.items.filter { it.isPurchased }

                    if (pending.isNotEmpty()) {
                        item { Text("To Buy (${pending.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp)) }
                        items(pending, key = { it.id }) { item ->
                            SwipeToDeleteBox(onDelete = { vm.deleteItem(item) }) {
                                ShoppingItemCard(item = item, onToggle = { vm.togglePurchased(item) })
                            }
                        }
                    }

                    if (purchased.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text("Purchased (${purchased.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp))
                        }
                        items(purchased, key = { it.id }) { item ->
                            SwipeToDeleteBox(onDelete = { vm.deleteItem(item) }) {
                                ShoppingItemCard(item = item, onToggle = { vm.togglePurchased(item) })
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Item", tint = Color.White)
        }
    }

    if (showDialog) {
        AddShoppingItemDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, quantity, unit, notes ->
                vm.addItem(name, quantity, unit, notes)
                showDialog = false
            }
        )
    }
}

@Composable
private fun ShoppingItemCard(item: ShoppingItemEntity, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (item.isPurchased) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(if (item.isPurchased) 0.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Checkbox(checked = item.isPurchased, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = if (item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                Text("${"%.1f".format(item.quantity)} ${item.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (item.notes.isNotEmpty()) Text(item.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddShoppingItemDialog(onDismiss: () -> Unit, onAdd: (String, Double, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("pcs") }
    var notes by remember { mutableStateOf("") }
    var unitExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Qty") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f), singleLine = true)
                    ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = it }, modifier = Modifier.weight(1f)) {
                        OutlinedTextField(value = unit, onValueChange = {}, readOnly = true, label = { Text("Unit") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable))
                        ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                            unitOptions.forEach { u -> DropdownMenuItem(text = { Text(u) }, onClick = { unit = u; unitExpanded = false }) }
                        }
                    }
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (optional)") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) onAdd(name.trim(), quantity.toDoubleOrNull() ?: 1.0, unit, notes.trim())
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
