package com.buildcal.probuild.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Neutral99,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Orange40,
    onSecondary = Neutral99,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Teal40,
    onTertiary = Neutral99,
    tertiaryContainer = Teal80,
    onTertiaryContainer = Neutral10,
    error = Red40,
    onError = Neutral99,
    errorContainer = Red80,
    onErrorContainer = Neutral10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = NeutralVariant90,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue40,
    onPrimaryContainer = Blue90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange40,
    onSecondaryContainer = Orange90,
    tertiary = Teal80,
    onTertiary = Neutral10,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant50
)

@Composable
fun BuildCalcProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
