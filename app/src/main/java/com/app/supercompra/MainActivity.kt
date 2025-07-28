package com.app.supercompra

import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompra.ui.theme.Marinho // Certifique-se de que este import está correto
import com.app.supercompra.ui.theme.SuperCompraTheme
import com.app.supercompra.ui.theme.Typography // Certifique-se de que este import está correto
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- 1. Data Classes (Modelos de Dados) ---
// Definem as estruturas de dados usadas em toda a aplicação.
data class ProdutoPedido(val nome: String, var quantidade: Int = 0)

data class Pedido(
    val produtos: List<ProdutoPedido>,
    val nomeCliente: String = "",
    val enderecoCliente: String = "",
    val dataPedido: String = ""
)

// --- 2. Sealed Class para Navegação de Telas ---
// Define todas as telas possíveis da sua aplicação para um controle de navegação centralizado.
sealed class Screen {
    object Welcome : Screen()
    object ListaCompras : Screen()
    data class PedidosRealizados(val pedido: Pedido? = null) : Screen()
    object Relatorios : Screen()
    object Cardapio : Screen()
}

// --- 3. MainActivity (A Única Activity da Aplicação Compose) ---
// Ponto de entrada da aplicação, responsável por hospedar a UI Composable.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o display de ponta a ponta para uma experiência imersiva
        setContent {
            SuperCompraTheme {
                // Estado observável que determina qual tela Composable está visível.
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
                var pedidoAtual by remember { mutableStateOf<Pedido?>(null) } // Estado para o pedido em edição/visualização

                // O Composable 'MainAppContent' gerencia a estrutura principal e a navegação.
                MainAppContent(
                    currentScreen = currentScreen,
                    // Lambda para outros Composables solicitarem uma mudança de tela.
                    onNavigateTo = { screen ->
                        if (screen is Screen.PedidosRealizados && screen.pedido != null) {
                            pedidoAtual = screen.pedido
                        }
                        currentScreen = screen // Atualiza o estado da tela atual, acionando a recomposição
                    },
                    // Lambda para o botão de voltar (físico ou da AppBar).
                    onBackClick = {
                        when (currentScreen) {
                            Screen.Cardapio -> currentScreen = Screen.ListaCompras
                            Screen.ListaCompras -> currentScreen = Screen.Welcome
                            is Screen.PedidosRealizados, Screen.Relatorios -> currentScreen = Screen.ListaCompras
                            Screen.Welcome -> finish() // Se estiver na tela de Boas-Vindas, fecha o app
                            else -> finish() // Comportamento padrão: fecha o app
                        }
                    },
                    currentPedido = pedidoAtual
                )
            }
        }
    }
}

// --- 4. MainAppContent Composable (Estrutura da UI Principal e Gerenciador de Telas) ---
// Contém a TopAppBar e um 'when' que exibe a tela Composable apropriada.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    currentScreen: Screen,
    onNavigateTo: (Screen) -> Unit,
    onBackClick: () -> Unit,
    currentPedido: Pedido?
) {
    Scaffold( // Fornece uma estrutura básica de Material Design
        topBar = {
            TopAppBar( // Barra superior com título e ícone de navegação
                title = { Text(text = "Doce Maria") },
                navigationIcon = {
                    // Exibe o botão de voltar apenas se não estiver na WelcomeScreen.
                    if (currentScreen != Screen.Welcome) {
                        IconButton(onClick = onBackClick) {
                            Image(
                                painter = painterResource(id = R.drawable.docemaria10),
                                contentDescription = "Voltar para a tela anterior",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Marinho, titleContentColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Aplica o preenchimento da Scaffold (para evitar sobreposição com TopAppBar)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Este 'when' é o coração da navegação, exibindo o Composable da tela atual.
            when (currentScreen) {
                is Screen.Welcome -> WelcomeScreen(onNavigateTo = onNavigateTo)
                is Screen.ListaCompras -> ListaComprasScreen(onNavigateTo)
                is Screen.PedidosRealizados -> PedidosRealizadosScreen(currentPedido)
                is Screen.Relatorios -> RelatoriosScreen()
                is Screen.Cardapio -> CardapioScreen(onNavigateTo)
            }
        }
    }
}

// --- 5. Telas Principais da Aplicação (Composables) ---
// Implementações das diferentes telas da sua aplicação.

@Composable
fun ListaComprasScreen(onNavigateTo: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageTopo(modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))

        PesquisaItem(
            modifier = Modifier
                .fillMaxWidth()
        )

        Titulo(
            texto = "Lista de produtos",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val produtosDisponiveis = remember {
            mutableStateListOf(
                ProdutoPedido("Torta de limão"),
                ProdutoPedido("Torta de Morango"),
                ProdutoPedido("Copo da Felicidade"),
                ProdutoPedido("Bolo de cenoura"),
                ProdutoPedido("Brownie"),
                ProdutoPedido("Doces Variados"),
                ProdutoPedido("Mini Bolos"),
                ProdutoPedido("Brigadeiros Gourmet"),
                ProdutoPedido("Cupcakes Decorados"),
                ProdutoPedido("Pudim de Leite Condensado"),
                ProdutoPedido("Cheesecake de Frutas Vermelhas"),
                ProdutoPedido("Pavê de Chocolate"),
                ProdutoPedido("Biscoitos Amanteigados"),
                ProdutoPedido("Macarons Coloridos")
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa o espaço restante verticalmente
                .verticalScroll(rememberScrollState()) // Habilita a rolagem
        ) {
            produtosDisponiveis.forEach { produto ->
                ItemDaLista(
                    textoItem = produto.nome,
                    initialQuantity = produto.quantidade,
                    onQuantityChange = { newQuantity ->
                        produto.quantidade = newQuantity
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
                onClick = {
                    val produtosSelecionados = produtosDisponiveis.filter { it.quantidade > 0 }
                    if (produtosSelecionados.isNotEmpty()) {
                        val novoPedido = Pedido(
                            produtos = produtosSelecionados,
                            dataPedido = getCurrentFormattedTime()
                        )
                        onNavigateTo(Screen.PedidosRealizados(novoPedido))
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Salvar Pedido")
            }
            Button(
                onClick = { onNavigateTo(Screen.Relatorios) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Relatórios")
            }
        }
    }
}

@Composable
fun PedidosRealizadosScreen(pedido: Pedido?) {
    var nomeCliente by remember { mutableStateOf(pedido?.nomeCliente ?: "") }
    var enderecoCliente by remember { mutableStateOf(pedido?.enderecoCliente ?: "") }
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
            value = nomeCliente,
            onValueChange = { nomeCliente = it },
            label = { Text("Nome do Cliente") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = enderecoCliente,
            onValueChange = { enderecoCliente = it },
            label = { Text("Endereço do Cliente") },
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
                Text("- ${produto.nome}: ${produto.quantidade}", style = Typography.bodySmall)
            }
        } else {
            Text("Não há pedido selecionado ou itens no carrinho.", style = Typography.bodyMedium)
        }

        Spacer(modifier = Modifier.size(24.dp))

        Button(onClick = { /* Lógica para salvar o pedido completo */ }) {
            Text("Confirmar Pedido")
        }
    }
}

@Composable
fun RelatoriosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Titulo(texto = "Relatórios")
        Text("Relatório de vendas e estoque.", style = Typography.bodyLarge)
    }
}

@Composable
fun CardapioScreen(onNavigateTo: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Titulo(texto = "Tabela de Valores")
        Image(
            painter = painterResource(id = R.drawable.cardapio), // Certifique-se de ter 'cardapio.png'
            contentDescription = "Cardápio Doce Maria",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Button(onClick = { onNavigateTo(Screen.ListaCompras) }) {
            Text("HOME")
        }
    }
}

// --- 6. Composables de Reutilizáveis (Utilitários de UI) ---
// Componentes menores que podem ser usados em várias telas.

@Composable
fun ImageTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.docemaria10), // Certifique-se de ter 'docemaria10.png'
        contentDescription = "Logo Super Compra",
        modifier = modifier.size(160.dp)
    )
}

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

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = Typography.headlineLarge,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

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

@Composable
fun ItemDaLista(
    modifier: Modifier = Modifier,
    textoItem: String,
    initialQuantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    var currentDateTime by remember { mutableStateOf(getCurrentFormattedTime()) }
    var isChecked by remember { mutableStateOf(initialQuantity > 0) }
    var quantity by remember { mutableStateOf(initialQuantity) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Atualiza a cada segundo
            currentDateTime = getCurrentFormattedTime()
        }
    }

    LaunchedEffect(initialQuantity) {
        isChecked = initialQuantity > 0
        quantity = initialQuantity
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    if (!it) { // Se desmarcado, zera a quantidade
                        quantity = 0
                        onQuantityChange(0)
                    }
                },
                modifier = Modifier.padding(end = 2.dp)
            )
            Text(
                text = textoItem,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f) // Ocupa o espaço restante
            )
            Spacer(modifier = Modifier.size(8.dp))

            if (isChecked) { // Mostra controles de quantidade se marcado
                MyIcon(icone = Icons.Default.Remove, onClick = {
                    if (quantity > 0) {
                        quantity--
                        onQuantityChange(quantity)
                    }
                })
                Text(
                    text = quantity.toString(),
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                MyIcon(icone = Icons.Default.Add, onClick = {
                    quantity++
                    onQuantityChange(quantity)
                })
                Spacer(modifier = Modifier.size(8.dp))
            }

            MyIcon(icone = Icons.Default.Edit, onClick = { /* TODO: Lógica de edição */ })
            Spacer(modifier = Modifier.size(8.dp))
            MyIcon(icone = Icons.Default.Delete, onClick = { /* TODO: Lógica de exclusão */ })
        }

        Text(
            text = currentDateTime,
            style = Typography.bodySmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 24.dp, top = 4.dp)
        )
    }
}

// --- 7. Funções de Utilitário (Lógica de Negócio Auxiliar) ---

fun getCurrentFormattedTime(): String {
    val localePtBr = Locale("pt", "BR")
    val sdf = SimpleDateFormat("EEEE - dd/MM/yyyy 'às' HH:mm:ss", localePtBr)
    return sdf.format(Date())
}

// --- 8. Preview Composables (Para visualização no Android Studio) ---

@Preview(showBackground = true) @Composable private fun ImageTopoPreview() { SuperCompraTheme { ImageTopo() } }
@Preview(showBackground = true) @Composable fun TituloPreview() { SuperCompraTheme { Titulo(texto = "Doçuras") } }
@Preview(showBackground = true) @Composable private fun ItemDaListaPreview() { SuperCompraTheme { ItemDaLista(textoItem = "Item de Exemplo", initialQuantity = 1, onQuantityChange = {}) } }
@Preview(showBackground = true) @Composable fun PedidosRealizadosScreenPreview() { SuperCompraTheme {
    val produtosExemplo = listOf(ProdutoPedido("Torta de limão", 2))
    // FIX: Completar o construtor Pedido com todos os parâmetros e fechar com ')'
    val pedidoExemplo = Pedido(produtos = produtosExemplo, nomeCliente = "Ana Paula", enderecoCliente = "Rua X, 123", dataPedido = getCurrentFormattedTime())
    PedidosRealizadosScreen(pedido = pedidoExemplo)
}}
@Preview(showBackground = true) @Composable fun RelatoriosScreenPreview() { SuperCompraTheme { RelatoriosScreen() } }
@Preview(showBackground = true) @Composable fun CardapioScreenPreview() { SuperCompraTheme { CardapioScreen(onNavigateTo = {}) } }

// O Preview da WelcomeScreen será colocado junto com a definição dela para melhor organização visual.