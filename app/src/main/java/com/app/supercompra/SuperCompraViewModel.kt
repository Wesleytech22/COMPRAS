package com.app.supercompra

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // Importante para atualizar o StateFlow de forma segura
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Classes de dados e Screen devem estar aqui (ou em arquivos separados, se preferir)
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

class SuperCompraViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Welcome)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _carrinho = MutableStateFlow<List<ProdutoPedido>>(emptyList())
    val carrinho: StateFlow<List<ProdutoPedido>> = _carrinho.asStateFlow()

    private val _pedidosRealizados = mutableListOf<Pedido>()
    val pedidosRealizados: List<Pedido>
        get() = _pedidosRealizados

    var pedidoEmVisualizacao by mutableStateOf<Pedido?>(null)
        private set

    val produtosDisponiveis = mutableListOf(
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

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun onBackPress(): Boolean {
        return when (_currentScreen.value) {
            Screen.Cardapio -> { _currentScreen.value = Screen.ListaCompras; true }
            Screen.Carrinho -> { _currentScreen.value = Screen.ListaCompras; true }
            Screen.ListaCompras -> { _currentScreen.value = Screen.Welcome; true }
            Screen.PedidosRealizados -> { _currentScreen.value = Screen.ListaCompras; true }
            Screen.Relatorios -> { _currentScreen.value = Screen.ListaCompras; true }
            Screen.ConhecaALoja -> { _currentScreen.value = Screen.Welcome; true }
            Screen.Welcome -> false
        }
    }

    fun adicionarOuIncrementarProdutoNoCarrinho(produto: ProdutoPedido) {
        _carrinho.update { currentList ->
            val existing = currentList.find { it.nome == produto.nome }
            if (existing != null) {
                currentList.map {
                    if (it.nome == produto.nome) existing.copy(quantidade = existing.quantidade + 1) else it
                }
            } else {
                currentList + produto.copy(quantidade = 1)
            }
        }
    }

    fun decrementarProdutoNoCarrinho(produto: ProdutoPedido) {
        _carrinho.update { currentList ->
            currentList.mapNotNull { item ->
                if (item.nome == produto.nome) {
                    if (item.quantidade > 1) {
                        item.copy(quantidade = item.quantidade - 1)
                    } else {
                        null // Remove o item se a quantidade chegar a 0
                    }
                } else {
                    item
                }
            }
        }
    }

    fun removerProdutoDoCarrinho(produto: ProdutoPedido) {
        _carrinho.update { currentList ->
            currentList.filter { it.nome != produto.nome }
        }
    }

    fun finalizarPedido(nomeCliente: String, enderecoCliente: String): Boolean {
        val currentCarrinho = _carrinho.value

        if (nomeCliente.isBlank() || enderecoCliente.isBlank() || currentCarrinho.isEmpty()) {
            return false
        }

        val novoPedido = Pedido(
            produtos = currentCarrinho.map { it.copy() },
            nomeCliente = nomeCliente,
            enderecoCliente = enderecoCliente,
            dataPedido = getCurrentFormattedTime()
        )

        pedidoEmVisualizacao = novoPedido
        _pedidosRealizados.add(novoPedido)
        _carrinho.value = emptyList()
        return true
    }

    fun editarPedido(pedido: Pedido) {
        _carrinho.value = pedido.produtos.map { it.copy() }
        _pedidosRealizados.remove(pedido)
    }

    fun getCurrentFormattedTime(): String {
        val localePtBr = Locale("pt", "BR")
        val sdf = SimpleDateFormat("EEEE - dd/MM/yyyy 'às' HH:mm:ss", localePtBr)
        return sdf.format(Date())
    }
}