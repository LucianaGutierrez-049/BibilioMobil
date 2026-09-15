package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RegistrarLibroUseCaseTest {
    @Test fun aceptaLibroValidoYRecortaTexto() = runTest {
        val repo = FakeLibroRepository()
        val libro = RegistrarLibroUseCase(repo)("  Rayuela ", " Julio Cortázar  ", "1963", "3").getOrThrow()
        assertEquals("Rayuela", libro.titulo); assertEquals("Julio Cortázar", libro.autor)
    }

    @Test fun tituloObligatorio() = runTest { assertEquals("El título es obligatorio", errores("", "Autor", "2000", "1").titulo) }
    @Test fun autorObligatorio() = runTest { assertEquals("El autor es obligatorio", errores("Libro", " ", "2000", "1").autor) }
    @Test fun anioObligatorio() = runTest { assertEquals("El año es obligatorio", errores("Libro", "Autor", "", "1").anio) }
    @Test fun anioDebeSerEntero() = runTest { assertEquals("El año debe ser un número entero", errores("Libro", "Autor", "dos mil", "1").anio) }
    @Test fun anioDebeEstarEnRango() = runTest { assertEquals("El año debe estar entre 1450 y 2026", errores("Libro", "Autor", "2027", "1").anio) }
    @Test fun ejemplaresObligatorios() = runTest { assertEquals("Los ejemplares son obligatorios", errores("Libro", "Autor", "2000", "").ejemplares) }
    @Test fun ejemplaresDebenSerEnteros() = runTest { assertEquals("Los ejemplares deben ser un número entero", errores("Libro", "Autor", "2000", "1.5").ejemplares) }
    @Test fun ejemplaresNoNegativos() = runTest { assertEquals("Los ejemplares no pueden ser negativos", errores("Libro", "Autor", "2000", "-1").ejemplares) }

    @Test fun enviaIdCeroYRepositorioAsignaId() = runTest {
        val repo = FakeLibroRepository()
        val libro = RegistrarLibroUseCase(repo)("Libro", "Autor", "2000", "1").getOrThrow()
        assertEquals(0L, repo.ultimoRecibido?.id); assertEquals(1L, libro.id)
    }

    @Test fun falloDelRepositorioLlegaComoFailure() = runTest {
        val repo = FakeLibroRepository().apply { fallarAlRegistrar = true }
        assertTrue(RegistrarLibroUseCase(repo)("Libro", "Autor", "2000", "1").isFailure)
    }

    @Test fun resultadoDeRelanzaCancelacion() = runTest {
        assertFailsWith<CancellationException> { resultadoDe<Unit> { throw CancellationException("cancelado") } }
    }

    private suspend fun errores(titulo: String, autor: String, anio: String, ejemplares: String): ErroresDeLibro =
        assertIs<LibroInvalidoException>(RegistrarLibroUseCase(FakeLibroRepository())(titulo, autor, anio, ejemplares).exceptionOrNull()).errores
}
