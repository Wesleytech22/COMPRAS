package com.app.supercompra

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.* // Mantendo as importações de Material3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompra.ui.theme.SuperCompraTheme

@Composable
fun AboutUsScreen(onNavigateBack: () -> Unit) { // onNavigateBack agora é para o botão global
    // O Scaffold com TopAppBar será gerenciado pela MainActivity,
    // garantindo um único botão de retorno no TopAppBar global.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Padding geral para a tela
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Alinhamento para os elementos fixos no topo
    ) {
        Image(
            painter = painterResource(id = R.drawable.docemaria10), // Você pode usar uma imagem diferente aqui se tiver uma foto da Giovanna
            contentDescription = "Foto da Giovanna Batista dos Santos",
            modifier = Modifier
                .size(180.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Olá, sou Giovanna Batista dos Santos!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Esta Column será a área rolável
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa o restante do espaço vertical
                .verticalScroll(rememberScrollState()), // Aplica o scroll apenas aqui
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Desde muito jovem, sempre fui apaixonada por confeitaria. O cheiro de bolo assando, o brilho do chocolate derretido e a alegria que um doce pode trazer para as pessoas sempre me encantaram. Comecei experimentando receitas em casa, para a família e amigos, e o feedback que recebia me motivava a ir além.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "A Doce Maria nasceu de um sonho de compartilhar essa paixão com um público maior. Cada receita, cada ingrediente, cada detalhe é escolhido com muito carinho e dedicação. Acredito que a comida, especialmente os doces, tem o poder de aquecer o coração e criar memórias especiais.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Meu objetivo é oferecer produtos de alta qualidade, feitos com ingredientes frescos e muito amor. Quero que cada mordida na Doce Maria seja uma experiência deliciosa e inesquecível para você. Obrigada por fazer parte dessa jornada e por confiar no meu trabalho!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsScreenPreview() {
    SuperCompraTheme {
        AboutUsScreen(onNavigateBack = {})
    }
}