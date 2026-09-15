package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val tieneErrores: Boolean get() = listOf(nombre, correo, telefono).any { it != null }
}

class LectorInvalidoException(val errores: ErroresDeLector) :
    IllegalArgumentException("El lector no cumple las reglas de registro")

private val FORMATO_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

class RegistrarLectorUseCase(private val repository: LectorRepository) {
    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> {
        val telefonoLimpio = telefono.trim().ifBlank { null }
        val errores = ErroresDeLector(
            nombre = if (nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = when {
                correo.isBlank() -> "El correo es obligatorio"
                !FORMATO_CORREO.matches(correo.trim()) -> "El correo no tiene un formato válido"
                else -> null
            },
            telefono = if (telefonoLimpio != null && !Regex("^\\d{6,9}$").matches(telefonoLimpio)) {
                "El teléfono debe tener entre 6 y 9 dígitos"
            } else null
        )
        if (errores.tieneErrores) return Result.failure(LectorInvalidoException(errores))

        return resultadoDe {
            repository.registrar(
                Lector(
                    id = 0L,
                    nombre = nombre.trim(),
                    correo = correo.trim(),
                    telefono = telefonoLimpio
                )
            )
        }
    }
}
