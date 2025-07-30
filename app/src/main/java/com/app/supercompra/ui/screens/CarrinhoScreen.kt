package com.app.supercompra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.Screen
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.MyIcon
import com.app.supercompra.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarrinhoScreen(
    viewModel: SuperCompraViewModel
) {
    var nomeCliente by remember { mutableStateOf("") }
    var enderecoCliente by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val carrinho = viewModel.carrinho

    val totalCarrinho = carrinho.sumOf { it.quantidade * it.preco }
    val formattedTotal = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(totalCarrinho)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Titulo(texto = "Seu Carrinho")
        Spacer(modifier = Modifier.size(16.dp))

        if (carrinho.isEmpty()) {
            Text("Seu carrinho está vazio. Adicione itens da lista de compras!", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.size(24.dp))
            Button(onClick = { viewModel.navigateTo(Screen.ListaCompras) }) {
                Text("Voltar para Compras")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .fillMaxWidth()
            ) {
                items(carrinho) { produto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = produto.nome, style = Typography.bodyMedium)
                            Text(
                                text = "Preço: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.preco)}",
                                style = Typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        MyIcon(icone = Icons.Default.Remove, onClick = {
                            viewModel.decrementarProdutoNoCarrinho(produto)
                        })
                        Text(
                            text = produto.quantidade.toString(),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        MyIcon(icone = Icons.Default.Add, onClick = {
                            viewModel.adicionarOuIncrementarProdutoNoCarrinho(produto)
                        })
                        Spacer(modifier = Modifier.size(8.dp))
                        MyIcon(icone = Icons.Default.Delete, onClick = {
                            viewModel.removerProdutoDoCarrinho(produto)
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = "Total do Carrinho: $formattedTotal",
                style = Typography.headlineMedium,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.size(24.dp))

            Titulo(texto = "Dados do Cliente")
            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                value = nomeCliente,
                onValueChange = { nomeCliente = it },
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                value = enderecoCliente,
                onValueChange = { enderecoCliente = it },
                label = { Text("Endereço de Entrega") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.size(24.dp))

            Button(
                onClick = {
                    val success = viewModel.finalizarPedido(nomeCliente, enderecoCliente)
                    if (success) {
                        viewModel.navigateTo(Screen.PedidosRealizados)
                        nomeCliente = ""
                        enderecoCliente = ""
                    } else {
                        println("Erro ao finalizar pedido: Preencha todos os campos e adicione itens ao carrinho.")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = carrinho.isNotEmpty()
            ) {
                Text("Finalizar Pedido")
            }

            Spacer(modifier = Modifier.imePadding())
        }
    }
}

