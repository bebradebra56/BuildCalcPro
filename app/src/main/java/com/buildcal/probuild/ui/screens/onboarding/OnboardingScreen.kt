package com.buildcal.probuild.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.ui.navigation.Screen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val gradientColors: List<Color>
)

private val pages = listOf(
    OnboardingPage(
        Icons.Filled.Calculate,
        "Calculate Building Materials",
        "Instantly calculate bricks, concrete, tiles, paint and more with precise formulas.",
        listOf(Color(0xFF1565C0), Color(0xFF0288D1))
    ),
    OnboardingPage(
        Icons.Filled.AttachMoney,
        "Plan Construction Costs",
        "Get accurate cost estimates. Know your budget before you start building.",
        listOf(Color(0xFFE65100), Color(0xFFF57C00))
    ),
    OnboardingPage(
        Icons.Filled.FolderOpen,
        "Save Your Building Projects",
        "Organize multiple projects, rooms, and measurements. Access them anytime.",
        listOf(Color(0xFF00695C), Color(0xFF00897B))
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val settingsDataStore = koinInject<SettingsDataStore>()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = pages[pageIndex]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(page.gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(page.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                    }
                    Text(
                        page.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        page.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(pages.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == index) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                if (pagerState.currentPage == pages.size - 1) {
                    Button(
                        onClick = {
                            scope.launch {
                                settingsDataStore.setOnboardingCompleted(true)
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Get Started", modifier = Modifier.padding(vertical = 4.dp))
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    settingsDataStore.setOnboardingCompleted(true)
                                    navController.navigate(Screen.Dashboard.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            }
                        ) { Text("Skip") }

                        Button(
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Next")
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
