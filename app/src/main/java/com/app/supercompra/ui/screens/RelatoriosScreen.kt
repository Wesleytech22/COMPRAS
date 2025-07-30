package com.app.supercompra.ui.screens // PACOTE CORRETO

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.theme.Typography

@Composable
fun RelatoriosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Titulo(texto = "Relatórios")
        Text("Relatório de vendas e estoque.", style = Typography.bodyLarge)
    }
}

