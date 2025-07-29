package com.app.supercompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompra.ui.theme.Marinho
import com.app.supercompra.ui.theme.SuperCompraTheme
import com.app.supercompra.ui.theme.Typography
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Seus data classes e sealed class Screen (mantidos como estão)
data class ProdutoPedido(val nome: String, var quantidade: Int = 0, val preco: Double = 0.0)

data class Pedido(
    val produtos: List<ProdutoPedido>,
    val nomeCliente: String = "",
    val enderecoCliente: String = "",
    val dataPedido: String = ""
)

sealed class Screen {
    object Welcome : Screen()
    object ListaCompras : Screen()
    object Carrinho : Screen()
    object PedidosRealizados : Screen()
    object Relatorios : Screen()
    object Cardapio : Screen()
    object ConhecaALoja : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperCompraTheme {
                val context = LocalContext.current
                val sunmiPrintHelper = remember { SunmiPrintHelper(context) }

                DisposableEffect(Unit) {
                    sunmiPrintHelper.initPrinter()
                    onDispose {
                        sunmiPrintHelper.deinitPrinter()
                    }
                }

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
                val carrinho = remember { mutableStateListOf<ProdutoPedido>() }
                var pedidoEmEdicao by remember { mutableStateOf<Pedido?>(null) }

                MainAppContent(
                    currentScreen = currentScreen,
                    onNavigateTo = { screen ->
                        currentScreen = screen
                    },
                    onBackClick = {
                        when (currentScreen) {
                            Screen.Cardapio -> currentScreen = Screen.ListaCompras
                            Screen.Carrinho -> currentScreen = Screen.ListaCompras
                            Screen.ListaCompras -> currentScreen = Screen.Welcome
                            Screen.PedidosRealizados -> currentScreen = Screen.ListaCompras
                            Screen.Relatorios -> currentScreen = Screen.ListaCompras
                            Screen.ConhecaALoja -> currentScreen = Screen.Welcome
                            Screen.Welcome -> finish()
                        }
                    },
                    carrinho = carrinho,
                    onSetCurrentPedido = { pedido -> pedidoEmEdicao = pedido },
                    currentPedido = pedidoEmEdicao,
                    onEditPedidoClick = { pedidoParaEditar ->
                        carrinho.clear()
                        carrinho.addAll(pedidoParaEditar.produtos)
                        currentScreen = Screen.Carrinho
                    },
                    onPrintPedidoClick = { pedidoParaImprimir ->
                        sunmiPrintHelper.printText(formatPedidoToPrint(pedidoParaImprimir))
                    },
                    sunmiPrintHelper = sunmiPrintHelper
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    currentScreen: Screen,
    onNavigateTo: (Screen) -> Unit,
    onBackClick: () -> Unit,
    carrinho: MutableList<ProdutoPedido>,
    onSetCurrentPedido: (Pedido?) -> Unit,
    currentPedido: Pedido?,
    onEditPedidoClick: (Pedido) -> Unit,
    onPrintPedidoClick: (Pedido) -> Unit,
    sunmiPrintHelper: SunmiPrintHelper
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Doce Maria") },
                navigationIcon = {
                    if (currentScreen != Screen.Welcome) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar para a tela anterior",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (currentScreen == Screen.ListaCompras || currentScreen == Screen.Carrinho) {
                        IconButton(onClick = { onNavigateTo(Screen.Carrinho) }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrinho de Compras",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
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
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentScreen) {
                is Screen.Welcome -> WelcomeScreen(onNavigateTo = onNavigateTo)
                is Screen.ListaCompras -> ListaComprasScreen(
                    onNavigateTo = onNavigateTo,
                    carrinho = carrinho
                )
                is Screen.Carrinho -> CarrinhoScreen(
                    carrinho = carrinho,
                    onNavigateTo = onNavigateTo,
                    onSavePedido = onSetCurrentPedido
                )
                is Screen.PedidosRealizados -> PedidosRealizadosScreen(
                    pedido = currentPedido,
                    onEditPedidoClick = onEditPedidoClick,
                    onPrintPedidoClick = onPrintPedidoClick,
                    sunmiPrintHelper = sunmiPrintHelper,
                    onNavigateTo = onNavigateTo
                )
                is Screen.Relatorios -> RelatoriosScreen()
                is Screen.Cardapio -> CardapioScreen(onNavigateTo)
                is Screen.ConhecaALoja -> AboutUsScreen(onNavigateBack = onBackClick)
            }
        }
    }
}

@Composable
fun ListaComprasScreen(
    onNavigateTo: (Screen) -> Unit,
    carrinho: MutableList<ProdutoPedido>
) {
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
                ProdutoPedido("Torta de limão", preco = 35.00),
                ProdutoPedido("Torta de Morango", preco = 40.00),
                ProdutoPedido("Copo da Felicidade", preco = 15.00),
                ProdutoPedido("Bolo de cenoura", preco = 50.00),
                ProdutoPedido("Brownie", preco = 10.00),
                ProdutoPedido("Doces Variados", preco = 5.00),
                ProdutoPedido("Mini Bolos", preco = 25.00),
                ProdutoPedido("Brigadeiros Gourmet", preco = 4.00),
                ProdutoPedido("Cupcakes Decorados", preco = 12.00),
                ProdutoPedido("Pudim de Leite Condensado", preco = 30.00),
                ProdutoPedido("Cheesecake de Frutas Vermelhas", preco = 45.00),
                ProdutoPedido("Pavê de Chocolate", preco = 38.00),
                ProdutoPedido("Biscoitos Amanteigados", preco = 8.00),
                ProdutoPedido("Macarons Coloridos", preco = 6.00)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            produtosDisponiveis.forEach { produtoDisponivel ->
                val produtoNoCarrinho = carrinho.find { it.nome == produtoDisponivel.nome }
                // ItemDaLista agora lida com o clique e exibe a quantidade do carrinho
                ItemDaLista(
                    produto = produtoDisponivel,
                    // Passa a quantidade atual do carrinho para exibição
                    initialQuantity = produtoNoCarrinho?.quantidade ?: 0,
                    // onQuantityChange não será mais usado diretamente pelos botões aqui,
                    // mas pode ser mantido caso queira um checkbox que adicione 1 unidade.
                    // A lógica principal de adição será no onItemClick.
                    onQuantityChange = { }, // Não faz nada ao mudar a quantidade aqui
                    modifier = Modifier.fillMaxWidth(),
                    onItemClick = {
                        val existing = carrinho.find { it.nome == produtoDisponivel.nome }
                        if (existing != null) {
                            // Se o item já está no carrinho, incrementa a quantidade
                            existing.quantidade++
                        } else {
                            // Se o item não está no carrinho, adiciona com quantidade 1
                            carrinho.add(produtoDisponivel.copy(quantidade = 1))
                        }
                        onNavigateTo(Screen.Carrinho) // Navega para o carrinho após adicionar/incrementar
                    }
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
                onClick = { onNavigateTo(Screen.Carrinho) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Ver Carrinho")
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
fun CarrinhoScreen(
    carrinho: MutableList<ProdutoPedido>,
    onNavigateTo: (Screen) -> Unit,
    onSavePedido: (Pedido?) -> Unit
) {
    var nomeCliente by remember { mutableStateOf("") }
    var enderecoCliente by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

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
            Button(onClick = { onNavigateTo(Screen.ListaCompras) }) {
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
                            Text(text = "Preço: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.preco)}", style = Typography.bodySmall, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        MyIcon(icone = Icons.Default.Remove, onClick = {
                            if (produto.quantidade > 1) {
                                produto.quantidade--
                            } else {
                                carrinho.remove(produto)
                            }
                        })
                        Text(text = produto.quantidade.toString(), style = Typography.bodyMedium, modifier = Modifier.padding(horizontal = 4.dp))
                        MyIcon(icone = Icons.Default.Add, onClick = {
                            produto.quantidade++
                        })
                        Spacer(modifier = Modifier.size(8.dp))
                        MyIcon(icone = Icons.Default.Delete, onClick = {
                            carrinho.remove(produto)
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
                    if (nomeCliente.isNotBlank() && enderecoCliente.isNotBlank() && carrinho.isNotEmpty()) {
                        val novoPedido = Pedido(
                            produtos = carrinho.toList(),
                            nomeCliente = nomeCliente,
                            enderecoCliente = enderecoCliente,
                            dataPedido = getCurrentFormattedTime()
                        )
                        onSavePedido(novoPedido)
                        onNavigateTo(Screen.PedidosRealizados)
                        carrinho.clear()
                        nomeCliente = ""
                        enderecoCliente = ""
                    } else {
                        // Você pode adicionar um Toast ou SnackBar aqui para avisar o usuário
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Pedido")
            }

            Spacer(modifier = Modifier.imePadding())
        }
    }
}

@Composable
fun PedidosRealizadosScreen(
    pedido: Pedido?,
    onEditPedidoClick: (Pedido) -> Unit,
    onPrintPedidoClick: (Pedido) -> Unit,
    sunmiPrintHelper: SunmiPrintHelper,
    onNavigateTo: (Screen) -> Unit
) {
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
            Text("Total do Pedido: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(totalPedido)}", style = Typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

        } else {
            Text("Não há pedido selecionado ou itens no carrinho.", style = Typography.bodySmall)
        }

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    if (pedido != null) {
                        onEditPedidoClick(pedido)
                        // A navegação para Screen.Carrinho já está em onEditPedidoClick na MainAppContent
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Editar Pedido")
            }
            Button(
                onClick = {
                    if (pedido != null) {
                        onPrintPedidoClick(pedido)
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Imprimir")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = { onNavigateTo(Screen.ListaCompras) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Novo Pedido")
        }
    }
}

// Sua função formatPedidoToPrint (mantida como está)
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
    sb.append("------------------------------------------\n")
    sb.append(String.format("TOTAL: %.2f\n", totalPedido(pedido))) // Chama a função totalPedido
    sb.append("------------------------------------------\n")
    sb.append("     Agradecemos sua preferência!\n")
    sb.append("------------------------------------------\n")
    return sb.toString()
}

// Adiciona uma função auxiliar para calcular o total do pedido
fun totalPedido(pedido: Pedido): Double {
    return pedido.produtos.sumOf { it.quantidade * it.preco }
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
            painter = painterResource(id = R.drawable.cardapio),
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

@Composable
fun ImageTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.docemaria10),
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
    produto: ProdutoPedido,
    initialQuantity: Int, // Quantidade atual do item no carrinho
    onQuantityChange: (Int) -> Unit, // Mantido para o Checkbox, mas os botões Add/Remove serão removidos
    onItemClick: () -> Unit // Novo: acionado ao clicar no item inteiro
) {
    // A data/hora parece ser um elemento decorativo em cada item.
    // Se a intenção é que seja a hora que o item foi adicionado ao carrinho,
    // essa lógica deveria estar ligada ao produto no carrinho.
    // Para a funcionalidade atual, o LaunchedEffect permanece.
    var currentDateTime by remember { mutableStateOf(getCurrentFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentDateTime = getCurrentFormattedTime()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() } // O clique no item inteiro aciona a lógica de adição/navegação
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox: pode ser usado para indicar se o item está no carrinho (quantidade > 0)
        // O clique no checkbox pode ser simplificado para apenas adicionar 1 ou remover
        Checkbox(
            checked = initialQuantity > 0,
            onCheckedChange = { isChecked ->
                // Se o checkbox é marcado e a quantidade é 0, adiciona 1.
                // Se desmarcado, remove o item.
                // A navegação para o carrinho ocorrerá no onItemClick.
                if (isChecked && initialQuantity == 0) {
                    onQuantityChange(1) // Isso faria a lógica de adicionar 1 no carrinho
                } else if (!isChecked && initialQuantity > 0) {
                    onQuantityChange(0) // Isso removeria do carrinho
                }
                onItemClick() // Navega para o carrinho
            },
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

        // Exibe a quantidade do item no carrinho (se houver)
        // Estes botões e o texto de quantidade NÃO DEVEM mais alterar a quantidade aqui.
        // O ajuste da quantidade agora é EXCLUSIVO do CarrinhoScreen.
        if (initialQuantity > 0) {
            Text(
                text = "No carrinho: ${initialQuantity}", // Texto para exibir a quantidade
                style = Typography.bodySmall,
                color = Marinho,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        // Os botões de Add, Remove, Edit, Delete NÃO devem aparecer aqui, apenas no CarrinhoScreen.
        // Se a intenção é exibir a quantidade do carrinho, o texto acima é suficiente.
        // Se esses ícones ainda precisam ter alguma função aqui, me diga qual.
        // Por enquanto, removi a funcionalidade deles em ItemDaLista conforme o requisito "somente no carrinho é possível ajustar a quantidade".
    }

    // Mantido o timestamp, se essa for a intenção.
    Text(
        text = currentDateTime,
        style = Typography.bodySmall,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(start = 24.dp, top = 4.dp)
    )
}

fun getCurrentFormattedTime(): String {
    val localePtBr = Locale("pt", "BR")
    val sdf = SimpleDateFormat("EEEE - dd/MM/yyyy 'às' HH:mm:ss", localePtBr)
    return sdf.format(Date())
}

@Preview(showBackground = true)
@Composable
private fun ImageTopoPreview() {
    SuperCompraTheme {
        ImageTopo()
    }
}

@Preview(showBackground = true)
@Composable
fun TituloPreview() {
    SuperCompraTheme {
        Titulo(texto = "Doçuras")
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemDaListaPreview() {
    SuperCompraTheme {
        ItemDaLista(
            produto = ProdutoPedido("Item de Exemplo", preco = 19.99),
            initialQuantity = 1,
            onQuantityChange = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PedidosRealizadosScreenPreview() {
    SuperCompraTheme {
        val produtosExemplo = listOf(ProdutoPedido("Torta de limão", 2, 35.00))
        val pedidoExemplo = Pedido(produtos = produtosExemplo, nomeCliente = "Ana Paula", enderecoCliente = "Rua X, 123", dataPedido = getCurrentFormattedTime())
        PedidosRealizadosScreen(
            pedido = pedidoExemplo,
            onEditPedidoClick = {},
            onPrintPedidoClick = {},
            sunmiPrintHelper = SunmiPrintHelper(LocalContext.current),
            onNavigateTo = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RelatoriosScreenPreview() {
    SuperCompraTheme {
        RelatoriosScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CardapioScreenPreview() {
    SuperCompraTheme {
        CardapioScreen(onNavigateTo = {})
    }
}