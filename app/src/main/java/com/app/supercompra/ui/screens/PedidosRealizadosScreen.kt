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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log 

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

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = pedido?.telefoneCliente ?: "N/A",
            onValueChange = { },
            label = { Text("Telefone") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(24.dp))

        Titulo(texto = "Detalhes do Pedido")
        Spacer(modifier = Modifier.size(16.dp))

        if (pedido != null && pedido.produtos.isNotEmpty()) {
            val localePtBr = Locale("pt", "BR")
            val sdfFull = SimpleDateFormat("EEEE - dd/MM/yyyy 'às' HH:mm:ss", localePtBr)
            val sdfDayOfWeek = SimpleDateFormat("EEEE", localePtBr)
            val sdfDate = SimpleDateFormat("dd/MM/yyyy", localePtBr)
            val sdfTime = SimpleDateFormat("HH:mm:ss 'HRS'", localePtBr)

            val parsedDate: Date? = try {
                sdfFull.parse(pedido.dataPedido)
            } catch (e: Exception) {
                null
            }

            if (parsedDate != null) {
                Text(text = "Dia da Semana: ${sdfDayOfWeek.format(parsedDate)}", style = Typography.bodySmall)
                Text(text = "Data do Pedido: ${sdfDate.format(parsedDate)}", style = Typography.bodySmall)
                Text(text = "Horário: ${sdfTime.format(parsedDate)}", style = Typography.bodySmall)
            } else {
                Text(text = "Data do Pedido: ${pedido.dataPedido}", style = Typography.bodySmall)
            }

            Spacer(modifier = Modifier.size(8.dp))
            Text("Produtos:", style = Typography.bodyMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(4.dp))

            pedido.produtos.forEach { produto ->
                Text(
                    text = "- ${produto.nome}: ${produto.quantidade} x ${NumberFormat.getCurrencyInstance(localePtBr).format(produto.preco)}",
                    style = Typography.bodySmall
                )
            }

            val totalPedido = pedido.produtos.sumOf { it.quantidade * it.preco }
            Text(
                "Total do Pedido: ${NumberFormat.getCurrencyInstance(localePtBr).format(totalPedido)}",
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
                        val textoParaImprimir = formatPedidoToPrint(pedido)
                        Log.d("PrintDebug", "Conteúdo a ser impresso:\n$textoParaImprimir")
                        sunmiPrintHelper.printText(textoParaImprimir)
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
    val localePtBr = Locale("pt", "BR")
    val sb = StringBuilder()

    val sdfPrint = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", localePtBr)
    val parsedDate: Date? = try {
        SimpleDateFormat("EEEE - dd/MM/yyyy 'às' HH:mm:ss", localePtBr).parse(pedido.dataPedido)
    } catch (e: Exception) {
        null
    }
    val formattedDateForPrint = parsedDate?.let { sdfPrint.format(it) } ?: pedido.dataPedido

    sb.append("         DOCE MARIA      \n")
    sb.append("-----------------------------\n")
    sb.append("Data do Pedido: $formattedDateForPrint\n")
    sb.append("Cliente: ${pedido.nomeCliente}\n")
    sb.append("Endereço: ${pedido.enderecoCliente}\n")
    sb.append("Telefone: ${formatPhoneNumberForPrint(pedido.telefoneCliente)}\n") // Formata o telefone para a impressão
    sb.append("-------------------------------\n")
    sb.append("PRODUTOS:\n")
    pedido.produtos.forEach { produto ->
        val itemTotal = produto.quantidade * produto.preco
        sb.append(String.format(
            localePtBr,
            "%-20s x %d = %s\n",
            produto.nome,
            produto.quantidade,
            NumberFormat.getCurrencyInstance(localePtBr).format(itemTotal)
        ))
    }
    sb.append("------------------------------\n")
    sb.append(String.format(
        localePtBr,
        "TOTAL: %s\n",
        NumberFormat.getCurrencyInstance(localePtBr).format(totalPedido(pedido))
    ))
    sb.append("------------------------------\n")
    sb.append("     Doce Maria Agradece!\n")
    sb.append("------------------------------\n")
    return sb.toString()
}

// Nova função para formatar o número de telefone para impressão (opcional, se quiser a máscara na impressão)
fun formatPhoneNumberForPrint(phoneNumber: String): String {
    val cleanNumber = phoneNumber.filter { it.isDigit() }
    return when (cleanNumber.length) {
        10 -> "(${cleanNumber.substring(0, 2)}) ${cleanNumber.substring(2, 6)}-${cleanNumber.substring(6, 10)}"
        11 -> "(${cleanNumber.substring(0, 2)}) ${cleanNumber.substring(2, 7)}-${cleanNumber.substring(7, 11)}"
        else -> phoneNumber // Retorna como está se não corresponder aos formatos esperados
    }
}

fun totalPedido(pedido: Pedido): Double {
    return pedido.produtos.sumOf { it.quantidade * it.preco }
}