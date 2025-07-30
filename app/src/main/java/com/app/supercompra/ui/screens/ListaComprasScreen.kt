package com.app.supercompra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.Screen
import com.app.supercompra.ui.ImageTopo
import com.app.supercompra.ui.PesquisaItem
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.ItemDaLista

@Composable
fun ListaComprasScreen(
    viewModel: SuperCompraViewModel
) {
    val produtosDisponiveis = viewModel.produtosDisponiveis
    val carrinho = viewModel.carrinho

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageTopo(modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))

        PesquisaItem(
            modifier = Modifier.fillMaxWidth()
        )

        Titulo(
            texto = "Lista de produtos",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(produtosDisponiveis) { produtoDisponivel ->
                val produtoNoCarrinho = carrinho.find { it.nome == produtoDisponivel.nome }
                val initialQuantity = produtoNoCarrinho?.quantidade ?: 0

                ItemDaLista(
                    produto = produtoDisponivel,
                    initialQuantity = initialQuantity,
                    onItemClick = {
                        viewModel.adicionarOuIncrementarProdutoNoCarrinho(produtoDisponivel)
                        viewModel.navigateTo(Screen.Carrinho)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { viewModel.navigateTo(Screen.Carrinho) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Ver Carrinho")
            }
            Button(
                onClick = { viewModel.navigateTo(Screen.Relatorios) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Relatórios")
            }
        }
    }
}

