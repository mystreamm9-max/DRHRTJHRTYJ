package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores fijo sin Material You, sin tintes, sin alpha
private val FixedColorScheme = darkColorScheme(
    primary = CasinoAcento,
    onPrimary = CasinoDevolucionTexto,
    primaryContainer = CasinoPanel,
    onPrimaryContainer = CasinoAcento,
    secondary = CasinoIngreso,
    onSecondary = Color.White,
    secondaryContainer = CasinoPanel,
    onSecondaryContainer = CasinoTextoPrincipal,
    tertiary = CasinoJackpot,
    onTertiary = Color.White,
    background = CasinoFondo,
    onBackground = CasinoTextoPrincipal,
    surface = CasinoPanel,
    onSurface = CasinoTextoPrincipal,
    surfaceVariant = CasinoPanel,
    onSurfaceVariant = CasinoTextoSecundario,
    outline = CasinoBorde,
    outlineVariant = CasinoBorde,
    error = CasinoRetiro,
    onError = Color.White
)

@Composable
fun CapriTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = CasinoFondo.toArgb()
                window.navigationBarColor = CasinoFondo.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = FixedColorScheme,
        typography = Typography,
        content = content
    )
}
