package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState = _uiState.asStateFlow()

    init { cargarLibros() }

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = FaseLibros.Cargando) }
            listarLibros().fold(
                onSuccess = { libros ->
                    _uiState.update {
                        it.copy(fase = if (libros.isEmpty()) FaseLibros.SinLibros else FaseLibros.ConLibros(libros.map { libro -> libro.aUi() }))
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(fase = FaseLibros.Error("No se pudo cargar el catálogo")) }
                }
            )
        }
    }

    fun onTituloChange(valor: String) = actualizarFormulario { it.copy(titulo = valor, errorTitulo = null) }
    fun onAutorChange(valor: String) = actualizarFormulario { it.copy(autor = valor, errorAutor = null) }
    fun onAnioChange(valor: String) = actualizarFormulario { it.copy(anio = valor, errorAnio = null) }
    fun onEjemplaresChange(valor: String) = actualizarFormulario { it.copy(ejemplares = valor, errorEjemplares = null) }

    fun registrar() {
        if (_uiState.value.registrando) return
        val formulario = _uiState.value.formulario
        _uiState.update { it.copy(registrando = true, mensajeExito = null) }
        viewModelScope.launch {
            registrarLibro(formulario.titulo, formulario.autor, formulario.anio, formulario.ejemplares).fold(
                onSuccess = { libro ->
                    _uiState.update {
                        it.copy(
                            formulario = FormularioLibro(),
                            registrando = false,
                            mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                        )
                    }
                    cargarLibros()
                },
                onFailure = { error ->
                    if (error is LibroInvalidoException) {
                        _uiState.update {
                            it.copy(
                                registrando = false,
                                formulario = it.formulario.copy(
                                    errorTitulo = error.errores.titulo,
                                    errorAutor = error.errores.autor,
                                    errorAnio = error.errores.anio,
                                    errorEjemplares = error.errores.ejemplares
                                )
                            )
                        }
                    } else {
                        _uiState.update { it.copy(registrando = false, fase = FaseLibros.Error("No se pudo cargar el catálogo")) }
                    }
                }
            )
        }
    }

    private fun actualizarFormulario(cambio: (FormularioLibro) -> FormularioLibro) {
        _uiState.update { it.copy(formulario = cambio(it.formulario), mensajeExito = null) }
    }
}
