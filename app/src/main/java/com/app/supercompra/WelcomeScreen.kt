import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.app.supercompra.Screen
import com.app.supercompra.ui.theme.SuperCompraTheme

// Este Composable é definido dentro do MainActivity.kt (ou um arquivo de Composables)
// Ele NÃO é uma Activity separada.

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, onNavigateTo: (Screen) -> Unit) {
    val context = LocalContext.current // Permite acesso ao contexto Android

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Usa a cor de fundo do tema
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Centraliza verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
        ) {
            Image(
                painter = painterResource(id = R.drawable.docemaria10), // Seu logo
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
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {
                        // Quando clicado, invoca o callback 'onNavigateTo'
                        // para que a MainActivity mude para a tela ListaCompras.
                        onNavigateTo(Screen.ListaCompras)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Clique aqui")
                }

                Button(
                    onClick = {
                        // Similarmente, navega para a tela de Cardápio.
                        onNavigateTo(Screen.Cardapio)
                    }
                ) {
                    Text("Cardápio")
                }
            }
        }
    }
}

// --- Preview Composable para WelcomeScreen ---
@Preview(showBackground = true) // Mostra a pré-visualização com um fundo
@Composable
fun WelcomeScreenPreview() {
    SuperCompraTheme {
        // No preview, passamos um lambda vazio para 'onNavigateTo'
        // pois nenhuma navegação real ocorrerá.
        WelcomeScreen(onNavigateTo = {})
    }
}