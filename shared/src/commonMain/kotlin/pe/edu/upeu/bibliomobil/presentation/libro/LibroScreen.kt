package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LibroScreen(viewModel: LibroViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsState()
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Catálogo de libros", style = MaterialTheme.typography.headlineSmall)
        FormularioLibroCard(estado, viewModel)
        estado.mensajeExito?.let { MensajeExito(it) }
        when (val fase = estado.fase) {
            FaseLibros.Cargando -> Cargando("Cargando libros")
            FaseLibros.SinLibros -> EstadoVacio(Icons.Default.MenuBook, "Sin libros", "Registra el primer libro del catálogo.")
            is FaseLibros.ConLibros -> {
                Text(if (fase.libros.size == 1) "1 libro" else "${fase.libros.size} libros", style = MaterialTheme.typography.titleMedium)
                fase.libros.forEach { LibroCard(it) }
            }
            is FaseLibros.Error -> EstadoVacio(
                Icons.Default.MenuBook,
                fase.mensaje,
                "Intenta cargar el catálogo nuevamente.",
                color = MaterialTheme.colorScheme.error,
                accion = "Reintentar",
                onAccion = viewModel::cargarLibros
            )
        }
    }
}

@Composable
private fun FormularioLibroCard(estado: LibroUiState, viewModel: LibroViewModel) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Registrar libro", style = MaterialTheme.typography.titleLarge)
            ValidatedTextField(estado.formulario.titulo, viewModel::onTituloChange, "Título", estado.formulario.errorTitulo, Modifier.fillMaxWidth())
            ValidatedTextField(estado.formulario.autor, viewModel::onAutorChange, "Autor", estado.formulario.errorAutor, Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ValidatedTextField(estado.formulario.anio, viewModel::onAnioChange, "Año", estado.formulario.errorAnio, Modifier.weight(1f), KeyboardType.Number)
                ValidatedTextField(estado.formulario.ejemplares, viewModel::onEjemplaresChange, "Ejemplares", estado.formulario.errorEjemplares, Modifier.weight(1f), KeyboardType.Number)
            }
            Button(onClick = viewModel::registrar, enabled = !estado.registrando, modifier = Modifier.fillMaxWidth()) {
                Text(if (estado.registrando) "Registrando…" else "Registrar")
            }
        }
    }
}

@Composable
private fun LibroCard(libro: LibroUi) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(libro.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (libro.requiereReposicion) AssistChip(onClick = {}, label = { Text("Pocos ejemplares") })
            }
            Text(libro.autor)
            Text(libro.detalle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Cargando(texto: String) {
    Row(Modifier.fillMaxWidth().padding(24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(); Text(texto)
    }
}
