package com.app.supercompra.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Uma VisualTransformation para formatar números de telefone no formato (xx) xxxxx-xxxx.
 */
class TelefoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val maskedText = applyPhoneMask(originalText)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Mapeia a posição do cursor do texto original para o texto mascarado
                val cleanOffset = originalText.length.coerceAtMost(offset)
                var transformedOffset = cleanOffset

                if (cleanOffset > 0) transformedOffset += 1 // '('
                if (cleanOffset > 2) transformedOffset += 1 // ')'
                if (cleanOffset > 2) transformedOffset += 1 // ' '
                if (cleanOffset > 7) transformedOffset += 1 // '-'

                return transformedOffset.coerceAtMost(maskedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Mapeia a posição do cursor do texto mascarado de volta para o texto original
                val transformedOffset = offset.coerceAtMost(maskedText.length)
                var originalOffset = transformedOffset

                if (transformedOffset > 1) originalOffset -= 1 // '('
                if (transformedOffset > 4) originalOffset -= 1 // ')'
                if (transformedOffset > 5) originalOffset -= 1 // ' '
                if (transformedOffset > 10) originalOffset -= 1 // '-'

                return originalOffset.coerceAtLeast(0)
            }
        }

        return TransformedText(AnnotatedString(maskedText), offsetMapping)
    }

    private fun applyPhoneMask(text: String): String {
        val cleanText = text.filter { it.isDigit() } // Remove caracteres não numéricos
        val sb = StringBuilder()

        if (cleanText.isEmpty()) {
            return ""
        }

        // Aplica a máscara (xx) xxxxx-xxxx
        cleanText.forEachIndexed { index, char ->
            when (index) {
                0 -> sb.append("(")
                2 -> sb.append(") ")
                7 -> {
                    // Para números de 9 dígitos (depois do DDD), o hífen vem após o 5º dígito do número
                    // Se o número tem 11 dígitos no total (DDD + 9 dígitos), o hífen será depois do 7º char limpo
                    // Ex: 11987654321 -> (11) 98765-4321
                    if (cleanText.length == 11) { // Formato com 9º dígito
                        sb.append(char)
                        sb.append("-")
                        return@forEachIndexed // Pula para a próxima iteração após adicionar char e hífen
                    }
                    // Para números de 8 dígitos (DDD + 8 dígitos), o hífen vem após o 4º dígito do número
                    // Ex: 1123456789 -> (11) 2345-6789
                    // Não é o caso mais comum, mas para ser flexível.
                    // A lógica abaixo precisa ser cuidadosa para não duplicar o hífen.
                    // Onde o hífen vem?
                    // (XX) YYYYY-ZZZZ (Y=5 digitos, Z=4 digitos -> 11 digitos totais limpos)
                    // (XX) YYYY-ZZZZ (Y=4 digitos, Z=4 digitos -> 10 digitos totais limpos)

                    // Se for 9 digitos no numero principal, o hifen vem apos o 5o digito (indice 7 do cleanText)
                    // Se for 8 digitos no numero principal, o hifen vem apos o 4o digito (indice 6 do cleanText)
                }
            }
            sb.append(char)
        }

        // Lógica de formatação final para garantir o hífen e parênteses
        val formatted = sb.toString()
        val digitsOnly = formatted.filter { it.isDigit() }
        val finalSb = StringBuilder()

        if (digitsOnly.length >= 1) {
            finalSb.append("(")
            finalSb.append(digitsOnly.substring(0, digitsOnly.length.coerceAtMost(2)))
        }
        if (digitsOnly.length >= 3) {
            finalSb.append(") ")
            if (digitsOnly.length <= 7) { // (xx) xxxx (até 4 dígitos no número)
                finalSb.append(digitsOnly.substring(2, digitsOnly.length.coerceAtMost(6)))
                if (digitsOnly.length > 6) { // (xx) xxxx-
                    finalSb.append("-")
                    finalSb.append(digitsOnly.substring(6, digitsOnly.length.coerceAtMost(10)))
                }
            } else { // (xx) xxxxx-xxxx (5 dígitos no número)
                finalSb.append(digitsOnly.substring(2, digitsOnly.length.coerceAtMost(7)))
                if (digitsOnly.length > 7) {
                    finalSb.append("-")
                    finalSb.append(digitsOnly.substring(7, digitsOnly.length.coerceAtMost(11)))
                }
            }
        }
        return finalSb.toString()
    }
}