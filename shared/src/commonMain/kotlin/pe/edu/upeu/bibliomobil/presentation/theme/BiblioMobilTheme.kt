package pe.edu.upeu.bibliomobil.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Claro = lightColorScheme(
    primary = Color(0xFF765827), onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDFA5), onPrimaryContainer = Color(0xFF271900),
    secondary = Color(0xFF53643E), onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E9B9), onSecondaryContainer = Color(0xFF121F04),
    tertiary = Color(0xFF8A4F53), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDADB), onTertiaryContainer = Color(0xFF351014),
    background = Color(0xFFFFF8F1), onBackground = Color(0xFF201B15),
    surface = Color(0xFFFFF8F1), onSurface = Color(0xFF201B15),
    surfaceVariant = Color(0xFFEDE1D2), onSurfaceVariant = Color(0xFF4D463C),
    surfaceTint = Color(0xFF765827), inverseSurface = Color(0xFF352F29), inverseOnSurface = Color(0xFFFAEFE6), inversePrimary = Color(0xFFE9BF70),
    outline = Color(0xFF7F7669), outlineVariant = Color(0xFFD1C5B6), scrim = Color.Black,
    error = Color(0xFFBA1A1A), onError = Color.White, errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002),
    surfaceBright = Color(0xFFFFF8F1), surfaceDim = Color(0xFFE4D8CD),
    surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFFEF2E8),
    surfaceContainer = Color(0xFFF8ECE2), surfaceContainerHigh = Color(0xFFF2E6DC), surfaceContainerHighest = Color(0xFFECE0D6)
)

private val Oscuro = darkColorScheme(
    primary = Color(0xFFE9BF70), onPrimary = Color(0xFF402D00),
    primaryContainer = Color(0xFF5B4210), onPrimaryContainer = Color(0xFFFFDFA5),
    secondary = Color(0xFFB9CDA0), onSecondary = Color(0xFF253514),
    secondaryContainer = Color(0xFF3B4C29), onSecondaryContainer = Color(0xFFD6E9B9),
    tertiary = Color(0xFFFFB2B7), onTertiary = Color(0xFF531D24),
    tertiaryContainer = Color(0xFF6F353A), onTertiaryContainer = Color(0xFFFFDADB),
    background = Color(0xFF17130F), onBackground = Color(0xFFECE0D6),
    surface = Color(0xFF17130F), onSurface = Color(0xFFECE0D6),
    surfaceVariant = Color(0xFF4D463C), onSurfaceVariant = Color(0xFFD1C5B6),
    surfaceTint = Color(0xFFE9BF70), inverseSurface = Color(0xFFECE0D6), inverseOnSurface = Color(0xFF352F29), inversePrimary = Color(0xFF765827),
    outline = Color(0xFF998F82), outlineVariant = Color(0xFF4D463C), scrim = Color.Black,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005), errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6),
    surfaceBright = Color(0xFF3E3832), surfaceDim = Color(0xFF17130F),
    surfaceContainerLowest = Color(0xFF120E0A), surfaceContainerLow = Color(0xFF201B15),
    surfaceContainer = Color(0xFF241F19), surfaceContainerHigh = Color(0xFF2F2923), surfaceContainerHighest = Color(0xFF3A342E)
)

@Composable
fun BiblioMobilTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) Oscuro else Claro,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(14.dp),
            large = RoundedCornerShape(20.dp),
            extraLarge = RoundedCornerShape(28.dp)
        ),
        content = content
    )
}
