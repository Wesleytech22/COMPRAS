package com.app.supercompra

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompra.R
import com.app.supercompra.ui.theme.SuperCompraTheme

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, onNavigateTo: (Screen) -> Unit) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.docemaria10),
                contentDescription = "Logo Doce Maria",
                modifier = Modifier.size(250.dp)
            )
            Text(
                text = "Bem-vindo à Doce Maria!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "Faça seu pedido!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.whatsapp),
                    contentDescription = "WhatsApp Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(11) 9 5706-2806",
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {
                        onNavigateTo(Screen.ListaCompras)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Solicitar")
                }

                Button(
                    onClick = {
                        onNavigateTo(Screen.Cardapio)
                    }
                ) {
                    Text("Cardápio")
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        onNavigateTo(Screen.ConhecaALoja)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conheça a Loja")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SuperCompraTheme {
        WelcomeScreen(onNavigateTo = {})
    }
}