package com.app.supercompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// Importações para rolagem e alinhamento
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
// Se precisar dos ícones MyIcon novamente, descomente as linhas abaixo:
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.Delete
// import androidx.compose.material.icons.filled.Edit
// import androidx.compose.material3.Icon
// import androidx.compose.ui.graphics.vector.ImageVector

import com.app.supercompra.ui.theme.SuperCompraTheme
import com.app.supercompra.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperCompraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding) // Aplica o padding do Scaffold UMA VEZ na Column
                            .fillMaxSize() // Garante que a coluna preencha o espaço disponível
                            .verticalScroll(rememberScrollState()), // Permite rolagem se o conteúdo exceder a tela
                        horizontalAlignment = Alignment.CenterHorizontally // Centraliza o conteúdo (imagem e texto) horizontalmente
                    ) {
                        // ImageTopo com espaçamento superior e inferior
                        ImageTopo(modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))
                        // Titulo com espaçamento inferior
                        Titulo(modifier = Modifier.padding(bottom = 32.dp))

                        // Os ícones MyIcon e suas chamadas foram comentados para corresponder à sua imagem.
                        // Se quiser adicioná-los de volta, descomente as linhas abaixo
                        // e as importações relacionadas no topo do arquivo.
                        // MyIcon(icone = Icons.Default.Delete, modifier = Modifier.padding(bottom = 8.dp))
                        // MyIcon(icone = Icons.Default.Edit)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.logo_01), // Certifique-se que 'logo_01.png' está em res/drawable/
        contentDescription = "Logo Super Compra",
        modifier = modifier.size(160.dp)
    )
}

@Composable
fun Titulo(modifier: Modifier = Modifier, style: TextStyle = Typography.headlineLarge) {
    Text(
        text = "Lista de Compras",
        modifier = modifier,
        style = style
    )
}

// Composable MyIcon e suas Previews estão comentados, conforme sua última versão.
// Descomente se for utilizá-los.
//@Composable
//fun MyIcon(icone: ImageVector, modifier: Modifier = Modifier) {
//    Icon(
//        imageVector = icone,
//        contentDescription = "Ícone",
//        modifier = modifier.size(48.dp)
//    )
//}

// --- Funções de Preview ---
// As Previews para MyIcon também estão comentadas.
//@Preview
//@Composable
//private fun IconEditPreview() {
//    SuperCompraTheme {
//        MyIcon(icone = Icons.Default.Delete)
//    }
//}

@Preview
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
        Titulo()
    }
}