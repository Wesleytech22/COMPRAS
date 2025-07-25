package com.app.supercompra.ui.theme

import com.app.supercompra.R // Importação correta do R da sua aplicação para recursos de fonte
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color // Importação para usar a cor diretamente

// Criação da família de fontes a partir dos recursos
// Lembre-se: seus arquivos de fonte (ex: krona.ttf, numans.ttf) devem estar em 'app/src/main/res/font/'
// e seus nomes devem ser em MINÚSCULAS e válidos para recursos Android.
val kronaFontFamily = FontFamily(Font(R.font.krona))
val numansFontFamily = FontFamily(Font(R.font.numans))

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = kronaFontFamily,
        fontWeight = FontWeight.Bold, // Definido como negrito para corresponder à imagem
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        color = Coral // Definido para a cor Coral
    ),

    bodyLarge = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    labelSmall = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
)