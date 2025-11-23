package com.ct.junimostoreapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme( // Define el esquema de colores para el tema oscuro.
    primary = AmarilloMostaza,
    secondary = AzulCielo,
    tertiary = Pink80,

    onPrimary = Black, // Color del texto sobre el color primario.
    onSecondary = Black, // Color del texto sobre el color secundario.
    onTertiary = Black,
    onBackground = White, // Color del texto sobre el fondo.
    onSurface = White // Color del texto sobre las superficies (como tarjetas).
)

private val LightColorScheme = lightColorScheme( // Define el esquema de colores para el tema claro.
    primary = AmarilloMostaza,
    secondary = AzulCielo,
    tertiary = Pink40,

    onPrimary = Black,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun JunimoStoreAppTheme( // Función Composable que aplica el tema a la aplicación.
    darkTheme: Boolean = isSystemInDarkTheme(), // Determina si se debe usar el tema oscuro según la configuración del sistema.
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilita el color dinámico para usar el tema personalizado de la aplicación.
    content: @Composable () -> Unit // Contenido de la aplicación al que se le aplicará el tema.
) {
    val colorScheme = when { // Selecciona el esquema de colores a utilizar.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> { // Si el color dinámico está habilitado y es compatible.
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme // Si el tema oscuro está activado, usa el esquema de colores oscuro.
        else -> LightColorScheme // De lo contrario, usa el esquema de colores claro.
    }

    MaterialTheme( // Aplica el tema de Material 3.
        colorScheme = colorScheme, // Esquema de colores seleccionado.
        typography = Typography, // Estilos de tipografía definidos en Type.kt.
        content = content // Contenido de la aplicación.
    )
}