package com.app.supercompra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.supercompra.R
import com.app.supercompra.Screen

@Composable
fun AboutUsScreen(onNavigateBack: () -> Unit, onNavigateTo: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.docemaria10),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    onNavigateTo(Screen.ListaCompras)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fazer pedido")
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }
        }
    }
}