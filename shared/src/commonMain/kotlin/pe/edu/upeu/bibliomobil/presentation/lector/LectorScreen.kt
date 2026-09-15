package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
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
fun LectorScreen(viewModel: LectorViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsState()
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Cartera de lectores", style = MaterialTheme.typography.headlineSmall)
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Registrar lector", style = MaterialTheme.typography.titleLarge)
                ValidatedTextField(estado.formulario.nombre, viewModel::onNombreChange, "Nombre", estado.formulario.errorNombre, Modifier.fillMaxWidth())
                ValidatedTextField(estado.formulario.correo, viewModel::onCorreoChange, "Correo", estado.formulario.errorCorreo, Modifier.fillMaxWidth(), KeyboardType.Email)
                ValidatedTextField(estado.formulario.telefono, viewModel::onTelefonoChange, "Teléfono (opcional)", estado.formulario.errorTelefono, Modifier.fillMaxWidth(), KeyboardType.Phone)
                Button(onClick = viewModel::registrar, enabled = !estado.registrando, modifier = Modifier.fillMaxWidth()) { Text(if (estado.registrando) "Registrando…" else "Registrar") }
            }
        }
        estado.mensajeExito?.let { MensajeExito(it) }
        when (val fase = estado.fase) {
            FaseLectores.Cargando -> Row(Modifier.fillMaxWidth().padding(24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) { CircularProgressIndicator(); Text("Cargando lectores") }
            FaseLectores.SinLectores -> EstadoVacio(Icons.Default.People, "Sin lectores", "Registra el primer lector de la cartera.")
            is FaseLectores.ConLectores -> {
                Text(if (fase.lectores.size == 1) "1 lector" else "${fase.lectores.size} lectores", style = MaterialTheme.typography.titleMedium)
                fase.lectores.forEach { lector ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(lector.nombre, fontWeight = FontWeight.SemiBold)
                            Text(lector.correo)
                            Text(lector.telefono, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            is FaseLectores.Error -> EstadoVacio(Icons.Default.People, fase.mensaje, "Intenta cargar la cartera nuevamente.", color = MaterialTheme.colorScheme.error, accion = "Reintentar", onAccion = viewModel::cargarLectores)
        }
    }
}
