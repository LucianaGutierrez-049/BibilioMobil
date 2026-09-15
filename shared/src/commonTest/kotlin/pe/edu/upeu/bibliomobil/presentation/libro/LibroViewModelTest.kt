package pe.edu.upeu.bibliomobil.presentation.libro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest {
    private lateinit var dispatcher: TestDispatcher

    @BeforeTest fun preparar() { dispatcher = UnconfinedTestDispatcher(); Dispatchers.setMain(dispatcher) }
    @AfterTest fun restaurar() { Dispatchers.resetMain() }

    @Test fun arrancaEnSinLibros() = runTest { assertIs<FaseLibros.SinLibros>(viewModel(FakeLibroRepository()).uiState.value.fase) }

    @Test fun muestraFormatoExacto() = runTest {
        val repo = FakeLibroRepository(listOf(Libro(1, "Libro", "Autor", 1998, 3)))
        val fase = assertIs<FaseLibros.ConLibros>(viewModel(repo).uiState.value.fase)
        assertEquals("1998 · 3 ejemplares", fase.libros.single().detalle)
    }

    @Test fun pasaAErrorSiRepositorioFalla() = runTest {
        val repo = FakeLibroRepository().apply { fallarAlListar = true }
        val fase = assertIs<FaseLibros.Error>(viewModel(repo).uiState.value.fase)
        assertEquals("No se pudo cargar el catálogo", fase.mensaje)
    }

    @Test fun erroresDeValidacionCaenEnFormulario() = runTest {
        val vm = viewModel(FakeLibroRepository())
        vm.registrar()
        assertEquals("El título es obligatorio", vm.uiState.value.formulario.errorTitulo)
        assertIs<FaseLibros.SinLibros>(vm.uiState.value.fase)
    }

    @Test fun registrarLimpiaFormularioYRecarga() = runTest {
        val vm = viewModel(FakeLibroRepository())
        vm.onTituloChange("Rayuela"); vm.onAutorChange("Julio Cortázar"); vm.onAnioChange("1963"); vm.onEjemplaresChange("2")
        vm.registrar()
        assertEquals(FormularioLibro(), vm.uiState.value.formulario)
        assertEquals("Libro \"Rayuela\" registrado correctamente", vm.uiState.value.mensajeExito)
        assertIs<FaseLibros.ConLibros>(vm.uiState.value.fase)
    }

    @Test fun dobleToqueNoRegistraDosVeces() = runTest {
        val repo = FakeLibroRepository()
        val vm = viewModel(repo)
        vm.onTituloChange("Rayuela"); vm.onAutorChange("Julio Cortázar"); vm.onAnioChange("1963"); vm.onEjemplaresChange("2")
        vm.registrar(); vm.registrar()
        assertEquals(1, repo.registros)
    }

    private fun viewModel(repo: FakeLibroRepository) = LibroViewModel(RegistrarLibroUseCase(repo), ListarLibrosUseCase(repo))
}
