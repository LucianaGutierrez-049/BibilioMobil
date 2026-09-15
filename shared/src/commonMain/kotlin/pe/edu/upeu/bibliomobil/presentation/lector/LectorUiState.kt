package pe.edu.upeu.bibliomobil.presentation.lector

import pe.edu.upeu.bibliomobil.domain.model.Lector

data class FormularioLector(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorTelefono: String? = null
)

data class LectorUi(val id: Long, val nombre: String, val correo: String, val telefono: String)

fun Lector.aUi() = LectorUi(id, nombre, correo, telefono ?: "No registrado")

sealed interface FaseLectores {
    data object Cargando : FaseLectores
    data object SinLectores : FaseLectores
    data class ConLectores(val lectores: List<LectorUi>) : FaseLectores
    data class Error(val mensaje: String) : FaseLectores
}

data class LectorUiState(
    val fase: FaseLectores = FaseLectores.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)
