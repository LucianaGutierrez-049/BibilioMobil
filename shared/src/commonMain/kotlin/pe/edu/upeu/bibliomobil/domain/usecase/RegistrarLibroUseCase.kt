package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val tieneErrores: Boolean get() = listOf(titulo, autor, anio, ejemplares).any { it != null }
}

class LibroInvalidoException(val errores: ErroresDeLibro) :
    IllegalArgumentException("El libro no cumple las reglas de registro")

class RegistrarLibroUseCase(private val repository: LibroRepository) {
    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> {
        val anioValor = anio.toIntOrNull()
        val ejemplaresValor = ejemplares.toIntOrNull()
        val errores = ErroresDeLibro(
            titulo = if (titulo.isBlank()) "El título es obligatorio" else null,
            autor = if (autor.isBlank()) "El autor es obligatorio" else null,
            anio = when {
                anio.isBlank() -> "El año es obligatorio"
                anioValor == null -> "El año debe ser un número entero"
                anioValor !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO -> "El año debe estar entre 1450 y 2026"
                else -> null
            },
            ejemplares = when {
                ejemplares.isBlank() -> "Los ejemplares son obligatorios"
                ejemplaresValor == null -> "Los ejemplares deben ser un número entero"
                ejemplaresValor < 0 -> "Los ejemplares no pueden ser negativos"
                else -> null
            }
        )
        if (errores.tieneErrores) return Result.failure(LibroInvalidoException(errores))

        return resultadoDe {
            repository.registrar(
                Libro(
                    id = 0L,
                    titulo = titulo.trim(),
                    autor = autor.trim(),
                    anio = checkNotNull(anioValor),
                    ejemplares = checkNotNull(ejemplaresValor)
                )
            )
        }
    }
}
