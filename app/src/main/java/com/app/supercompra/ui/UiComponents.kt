package com.app.supercompra.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.supercompra.ProdutoPedido
import com.app.supercompra.R
import com.app.supercompra.SuperCompraViewModel
import com.app.supercompra.ui.theme.Marinho
import com.app.supercompra.ui.theme.Typography
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

// Função de componente de UI: Imagem do topo
@Composable
fun ImageTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.docemaria10),
        contentDescription = "Logo Super Compra",
        modifier = modifier.size(160.dp)
    )
}

// Função de componente de UI: Ícone genérico
@Composable
fun MyIcon(icone: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick, modifier = modifier.size(24.dp)) {
        Icon(
            imageVector = icone,
            contentDescription = "Ícone",
            tint = Marinho
        )
    }
}

// Função de componente de UI: Título formatado
@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = Typography.headlineLarge,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

// Função de componente de UI: Campo de pesquisa
@Composable
fun PesquisaItem(modifier: Modifier = Modifier) {
    var texto by remember { mutableStateOf("") }
    OutlinedTextField(
        value = texto,
        onValueChange = { texto = it },
        placeholder = {
            Text(
                text = "pesquise seu doce favorito",
                color = Color.Gray,
                style = Typography.bodyMedium
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        singleLine = true
    )
}

// Função de componente de UI: Item individual da lista de produtos
@Composable
fun ItemDaLista(
    modifier: Modifier = Modifier,
    produto: ProdutoPedido,
    initialQuantity: Int,
    onItemClick: () -> Unit
) {
    var currentDateTime by remember { mutableStateOf(SuperCompraViewModel().getCurrentFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentDateTime = SuperCompraViewModel().getCurrentFormattedTime()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = initialQuantity > 0,
            onCheckedChange = { },
            enabled = false,
            modifier = Modifier.padding(end = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = produto.nome,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Start,
            )
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.preco),
                style = Typography.bodySmall,
                textAlign = TextAlign.Start,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        if (initialQuantity > 0) {
            Text(
                text = "No carrinho: ${initialQuantity}",
                style = Typography.bodySmall,
                color = Marinho,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }

    Text(
        text = currentDateTime,
        style = Typography.bodySmall,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(start = 24.dp, top = 4.dp)
    )
}