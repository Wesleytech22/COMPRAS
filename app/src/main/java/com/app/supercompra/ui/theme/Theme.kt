package com.app.supercompra.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color // Importe Color se ainda não estiver importado

// As cores específicas do seu app (presumindo que Coral, CinzaClaro, etc., estão definidos em Colors.kt)
// Você pode precisar importá-las se não estiverem no mesmo arquivo
// import com.app.supercompra.ui.theme.Coral
// import com.app.supercompra.ui.theme.CinzaClaro
// import com.app.supercompra.ui.theme.CinzaEscuro
// import com.app.supercompra.ui.theme.Pink40
// import com.app.supercompra.ui.theme.Purple40
// import com.app.supercompra.ui.theme.Purple80
// import com.app.supercompra.ui.theme.PurpleGrey40
// import com.app.supercompra.ui.theme.PurpleGrey80

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Coral,
    secondary = Color.White, // <--- **MUDANÇA AQUI:** Alterado de 'Marinho' para uma cor neutra
    tertiary = Pink40,
    background = CinzaClaro, // <--- Esta é a cor de fundo que o Surface vai usar
    onBackground = CinzaEscuro // Corrigido para uma cor de texto/conteúdo contrastante
)

@Composable
fun SuperCompraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Cores dinâmicas disponíveis apenas no Android 12+ (API 31+)
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
        typography = Typography, // Usa a tipografia definida em Typography.kt
        content = content
    )
}