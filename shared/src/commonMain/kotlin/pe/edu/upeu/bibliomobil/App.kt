package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.navigation.screenDesdeClave
import pe.edu.upeu.bibliomobil.presentation.theme.BiblioMobilTheme

private val ScreenSaver = Saver<Screen, String>(save = { it.clave }, restore = ::screenDesdeClave)

@Composable
fun App() {
    KoinContext {
        val oscuroDelSistema = isSystemInDarkTheme()
        var oscuro by rememberSaveable { mutableStateOf(oscuroDelSistema) }
        BiblioMobilTheme(darkTheme = oscuro) {
            BiblioApp(oscuro = oscuro, onOscuroChange = { oscuro = it })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BiblioApp(oscuro: Boolean, onOscuroChange: (Boolean) -> Unit) {
    var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.Inicio) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val destinoActual = DESTINOS.first { it.screen == pantallaActual }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.fillMaxHeight().padding(12.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocalLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("BiblioMobil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider()
                    DESTINOS.forEach { destino ->
                        NavigationDrawerItem(
                            label = { Text(destino.titulo) },
                            icon = { Icon(destino.icono, contentDescription = null) },
                            selected = pantallaActual == destino.screen,
                            onClick = {
                                pantallaActual = destino.screen
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Modo oscuro") },
                        selected = false,
                        onClick = { onOscuroChange(!oscuro) },
                        badge = { Switch(checked = oscuro, onCheckedChange = onOscuroChange) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(destinoActual.titulo) },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Abrir menú") } }
                )
            }
        ) { padding ->
            val contenidoModifier = Modifier.fillMaxSize().padding(padding)
            when (pantallaActual) {
                Screen.Inicio -> InicioScreen(onNavegar = { pantallaActual = it }, modifier = contenidoModifier)
                Screen.Libros -> LibroScreen(viewModel = koinViewModel(), modifier = contenidoModifier)
                Screen.Lectores -> LectorScreen(viewModel = koinViewModel(), modifier = contenidoModifier)
                Screen.Prestamos -> EstadoVacio(
                    icono = Icons.Default.Bookmark,
                    titulo = "Préstamos en construcción",
                    descripcion = "Aquí podrás revisar y gestionar los préstamos de la biblioteca.",
                    modifier = contenidoModifier.padding(20.dp)
                )
            }
        }
    }
}
