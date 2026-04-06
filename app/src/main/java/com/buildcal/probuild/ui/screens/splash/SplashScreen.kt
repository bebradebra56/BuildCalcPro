package com.buildcal.probuild.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.ui.navigation.Screen
import com.buildcal.probuild.ui.theme.GradientEnd
import com.buildcal.probuild.ui.theme.GradientStart
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SplashScreen(navController: NavController) {
    val settingsDataStore = koinInject<SettingsDataStore>()
    val onboardingDone by settingsDataStore.onboardingCompleted.collectAsState(initial = false)

    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2000)
        navController.navigate(if (onboardingDone) Screen.Dashboard.route else Screen.Onboarding.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(GradientStart, GradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.scale(scale)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Calculate,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Build Calc Pro",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "Calculate materials for construction.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(500, delayMillis = 800)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CircularProgressIndicator(
                    color = Color.White.copy(alpha = 0.8f),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
                Text("Loading...", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}
