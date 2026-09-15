package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.fakes.FakeLectorRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RegistrarLectorUseCaseTest {
    @Test fun rechazaCorreoInvalido() = runTest {
        assertEquals("El correo no tiene un formato válido", errores("Ana", "correo-invalido", "").correo)
    }

    @Test fun rechazaTelefonoCorto() = runTest {
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", errores("Ana", "ana@upeu.edu.pe", "12345").telefono)
    }

    @Test fun telefonoEnBlancoSeGuardaComoNull() = runTest {
        val repo = FakeLectorRepository()
        RegistrarLectorUseCase(repo)("Ana", "ana@upeu.edu.pe", "   ").getOrThrow()
        assertEquals(null, repo.ultimoRecibido?.telefono)
    }

    @Test fun enviaIdCero() = runTest {
        val repo = FakeLectorRepository()
        RegistrarLectorUseCase(repo)("Ana", "ana@upeu.edu.pe", "987654321").getOrThrow()
        assertEquals(0L, repo.ultimoRecibido?.id)
    }

    @Test fun falloDelRepositorioLlegaComoFailure() = runTest {
        val repo = FakeLectorRepository().apply { fallarAlRegistrar = true }
        assertTrue(RegistrarLectorUseCase(repo)("Ana", "ana@upeu.edu.pe", "").isFailure)
    }

    private suspend fun errores(nombre: String, correo: String, telefono: String): ErroresDeLector =
        assertIs<LectorInvalidoException>(RegistrarLectorUseCase(FakeLectorRepository())(nombre, correo, telefono).exceptionOrNull()).errores
}
