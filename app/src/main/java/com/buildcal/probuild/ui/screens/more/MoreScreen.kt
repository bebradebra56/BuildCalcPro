package com.buildcal.probuild.ui.screens.more

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.navigation.Screen

private data class MoreItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@Composable
fun MoreScreen(navController: NavController) {

    val context = LocalContext.current
    val items = listOf(
        MoreItem("Materials", Icons.Filled.Inventory, Color(0xFF1565C0), Screen.Materials.route),
        MoreItem("Measurements", Icons.Filled.Straighten, Color(0xFF00695C), Screen.Measurements.route),
        MoreItem("Estimates", Icons.Filled.AttachMoney, Color(0xFFE65100), Screen.Estimates.route),
        MoreItem("Reports", Icons.Filled.Assessment, Color(0xFF6A1B9A), Screen.Reports.route),
        MoreItem("Calendar", Icons.Filled.CalendarMonth, Color(0xFF0277BD), Screen.Calendar.route),
        MoreItem("Tasks", Icons.Filled.Task, Color(0xFF558B2F), Screen.Tasks.route),
        MoreItem("Notifications", Icons.Filled.Notifications, Color(0xFFF57C00), Screen.Notifications.route),
        MoreItem("Activity", Icons.Filled.History, Color(0xFF546E7A), Screen.ActivityHistory.route),
        MoreItem("Profile", Icons.Filled.Person, Color(0xFF6D4C41), Screen.Profile.route),
        MoreItem("Settings", Icons.Filled.Settings, Color(0xFF455A64), Screen.Settings.route)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("More", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                MoreItemCard(item = item, onClick = { navController.navigate(item.route) })
            }
            item {
                MoreItemCard(item = MoreItem("Privacy Policy", Icons.Filled.Policy, Color(0xFF1565C0), Screen.Materials.route), onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://buiildcalcpro.com/privacy-policy.html"))
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Composable
private fun MoreItemCard(item: MoreItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(item.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, null, tint = item.color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(6.dp))
            Text(
                item.title,
                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 14.sp),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
