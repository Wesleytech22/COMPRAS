package com.app.supercompra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.SunmiPrintHelper
import com.app.supercompra.Pedido
import com.app.supercompra.Screen
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.theme.Typography
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PedidosRealizadosScreen(
    viewModel: SuperCompraViewModel,
    sunmiPrintHelper: SunmiPrintHelper
) {
    val pedido = viewModel.pedidoEmVisualizacao
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Titulo(texto = "Informações do Cliente")
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = pedido?.nomeCliente ?: "N/A",
            onValueChange = { },
            label = { Text("Nome do Cliente") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = pedido?.enderecoCliente ?: "N/A",
            onValueChange = { },
            label = { Text("Endereço do Cliente") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))

        Titulo(texto = "Detalhes do Pedido")
        Spacer(modifier = Modifier.size(16.dp))

        if (pedido != null && pedido.produtos.isNotEmpty()) {
            Text("Data do Pedido: ${pedido.dataPedido}", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.size(8.dp))
            Text("Produtos:", style = Typography.bodyMedium)
            pedido.produtos.forEach { produto ->
                Text("- ${produto.nome}: ${produto.quantidade} x ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.preco)}", style = Typography.bodySmall)
            }
            val totalPedido = pedido.produtos.sumOf { it.quantidade * it.preco }
            Text(
                "Total do Pedido: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(totalPedido)}",
                style = Typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

        } else {
            Text("Não há pedido selecionado para visualização.", style = Typography.bodySmall)
        }

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    if (pedido != null) {
                        viewModel.editarPedido(pedido)
                        viewModel.navigateTo(Screen.Carrinho)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                enabled = pedido != null
            ) {
                Text("Editar Pedido")
            }
            Button(
                onClick = {
                    if (pedido != null) {
                        sunmiPrintHelper.printText(formatPedidoToPrint(pedido))
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                enabled = pedido != null
            ) {
                Text("Imprimir")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = { viewModel.navigateTo(Screen.ListaCompras) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Novo Pedido")
        }
    }
}


fun formatPedidoToPrint(pedido: Pedido): String {
    val sb = StringBuilder()
    sb.append("         DOCE MARIA      \n")
    sb.append("-----------------------------\n")
    sb.append("Data do Pedido: ${pedido.dataPedido}\n")
    sb.append("Cliente: ${pedido.nomeCliente}\n")
    sb.append("Endereço: ${pedido.enderecoCliente}\n")
    sb.append("-------------------------------\n")
    sb.append("PRODUTOS:\n")
    pedido.produtos.forEach { produto ->
        val itemTotal = produto.quantidade * produto.preco
        sb.append(String.format(
            "%s x %d | %.2f = %.2f\n",
            produto.nome,
            produto.quantidade,
            produto.preco,
            itemTotal
        ))
    }
    sb.append("------------------------------\n")
    sb.append(String.format("TOTAL: %.2f\n", totalPedido(pedido)))
    sb.append("------------------------------\n")
    sb.append("     Doce Maria Agradece!\n")
    sb.append("------------------------------\n")
    return sb.toString()
}

fun totalPedido(pedido: Pedido): Double {
    return pedido.produtos.sumOf { it.quantidade * it.preco }
}

