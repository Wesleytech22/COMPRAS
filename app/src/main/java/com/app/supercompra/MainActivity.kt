package com.app.supercompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.supercompra.ui.theme.Marinho
import com.app.supercompra.ui.theme.SuperCompraTheme

// =========================================================
// IMPORTS DOS COMPONENTES DE UI (da pasta 'ui')
// =========================================================
import com.app.supercompra.ui.ImageTopo
import com.app.supercompra.ui.MyIcon
import com.app.supercompra.ui.Titulo
import com.app.supercompra.ui.PesquisaItem
import com.app.supercompra.ui.ItemDaLista

// =========================================================
// IMPORTS DAS TELAS (da pasta 'ui/screens')
// =========================================================
import com.app.supercompra.ui.screens.WelcomeScreen
import com.app.supercompra.ui.screens.AboutUsScreen
import com.app.supercompra.ui.screens.ListaComprasScreen
import com.app.supercompra.ui.screens.CarrinhoScreen
import com.app.supercompra.ui.screens.PedidosRealizadosScreen
import com.app.supercompra.ui.screens.RelatoriosScreen
import com.app.supercompra.ui.screens.CardapioScreen


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

                val viewModel: SuperCompraViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()

                MainAppContent(
                    viewModel = viewModel,
                    sunmiPrintHelper = sunmiPrintHelper
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    viewModel: SuperCompraViewModel,
    sunmiPrintHelper: SunmiPrintHelper
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Doce Maria") },
                navigationIcon = {
                    if (currentScreen != Screen.Welcome) {
                        IconButton(onClick = { viewModel.onBackPress() }) {
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
                        IconButton(onClick = { viewModel.navigateTo(Screen.Carrinho) }) {
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
                is Screen.Welcome -> WelcomeScreen(onNavigateTo = { viewModel.navigateTo(it) })
                is Screen.ListaCompras -> ListaComprasScreen(
                    viewModel = viewModel,
                )
                is Screen.Carrinho -> CarrinhoScreen(
                    viewModel = viewModel
                )
                is Screen.PedidosRealizados -> PedidosRealizadosScreen(
                    viewModel = viewModel,
                    sunmiPrintHelper = sunmiPrintHelper
                )
                is Screen.Relatorios -> RelatoriosScreen()
                is Screen.Cardapio -> CardapioScreen( // Linha 50:39 (se as outras foram movidas)
                    viewModel = viewModel
                )
                is Screen.ConhecaALoja -> AboutUsScreen(
                    onNavigateBack = { viewModel.onBackPress() },
                    onNavigateTo = { viewModel.navigateTo(it) }
                )
            }
        }
    }
}