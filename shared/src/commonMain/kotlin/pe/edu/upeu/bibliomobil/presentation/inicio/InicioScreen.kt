package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen

private data class OpcionInicio(val titulo: String, val detalle: String, val icono: ImageVector, val screen: Screen)

private val OPCIONES_INICIO = listOf(
    OpcionInicio("Registrar libros", "Amplía el catálogo bibliográfico", Icons.Default.MenuBook, Screen.Libros),
    OpcionInicio("Registrar lectores", "Incorpora nuevos usuarios", Icons.Default.PersonAdd, Screen.Lectores),
    OpcionInicio("Revisar préstamos", "Consulta el próximo módulo", Icons.Default.Bookmark, Screen.Prestamos)
)

@Composable
fun InicioScreen(onNavegar: (Screen) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.extraLarge) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocalLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("BiblioMobil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Tu biblioteca, organizada y siempre cerca")
                }
            }
        }
        Text("Qué puedes hacer", style = MaterialTheme.typography.titleLarge)
        OPCIONES_INICIO.forEach { opcion ->
            ElevatedCard(modifier = Modifier.fillMaxWidth().clickable { onNavegar(opcion.screen) }) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(opcion.icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Column { Text(opcion.titulo, fontWeight = FontWeight.SemiBold); Text(opcion.detalle, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
    }
}
