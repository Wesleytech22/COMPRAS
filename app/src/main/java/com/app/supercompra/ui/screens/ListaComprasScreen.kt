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
import androidx.compose.runtime.collectAsState // Importar collectAsState
import androidx.compose.runtime.getValue // Importar getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.Screen
import com.app.supercompra.ui.ImageTopo
import com.app.supercompra.ui.PesquisaItem
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.ItemDaLista
import com.app.supercompra.ProdutoPedido // Certifique-se de que ProdutoPedido está importado

@Composable
fun ListaComprasScreen(
    viewModel: SuperCompraViewModel
) {
    val produtosDisponiveis = viewModel.produtosDisponiveis

    // --- CORREÇÃO AQUI: Coletar o StateFlow do carrinho ---
    val carrinho by viewModel.carrinho.collectAsState()
    // --- FIM DA CORREÇÃO ---

    // Calcula o total de itens no carrinho para exibir no botão "Ver Carrinho"
    val totalItemsNoCarrinho = carrinho.sumOf { it.quantidade }

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
                // 'carrinho' agora é a List<ProdutoPedido>, então 'find' funciona
                val produtoNoCarrinho = carrinho.find { it.nome == produtoDisponivel.nome }
                val initialQuantity = produtoNoCarrinho?.quantidade ?: 0

                ItemDaLista(
                    produto = produtoDisponivel,
                    initialQuantity = initialQuantity,
                    onItemClick = {
                        viewModel.adicionarOuIncrementarProdutoNoCarrinho(produtoDisponivel)
                        // --- ALTERAÇÃO AQUI: Removido a navegação para o carrinho ao clicar ---
                        // A contagem no TopAppBar já irá refletir a adição
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
                // Exibe a contagem no botão "Ver Carrinho"
                Text("Ver Carrinho (${totalItemsNoCarrinho})")
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