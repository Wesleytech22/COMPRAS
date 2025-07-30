package com.app.supercompra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.supercompra.R
import com.app.supercompra.Screen
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.ui.Titulo

@Composable
fun CardapioScreen(viewModel: SuperCompraViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Titulo(texto = "Tabela de Valores")
        Image(
            painter = painterResource(id = R.drawable.cardapio),
            contentDescription = "Cardápio Doce Maria",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Button(onClick = { viewModel.navigateTo(Screen.ListaCompras) }) {
            Text("HOME")
        }
    }
}

