package pe.edu.upeu.bibliomobil.di

import org.koin.core.context.stopKoin
import org.koin.core.context.startKoin
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class AppModuleTest {
    @BeforeTest fun iniciar() { startKoin { modules(dataModule, domainModule) } }
    @AfterTest fun detener() { stopKoin() }

    @Test fun resuelveRepositorioPorInterfaz() { assertIs<LibroRepositorioEnMemoria>(org.koin.mp.KoinPlatform.getKoin().get<LibroRepository>()) }
    @Test fun repositorioEsUnico() {
        val koin = org.koin.mp.KoinPlatform.getKoin()
        assertSame(koin.get<LibroRepository>(), koin.get<LibroRepository>())
    }
    @Test fun resuelveCasosDeUsoDeLibro() {
        val koin = org.koin.mp.KoinPlatform.getKoin()
        koin.get<RegistrarLibroUseCase>(); koin.get<ListarLibrosUseCase>()
    }
    @Test fun resuelveCasosDeUsoDeLector() {
        val koin = org.koin.mp.KoinPlatform.getKoin()
        koin.get<RegistrarLectorUseCase>(); koin.get<ListarLectoresUseCase>()
    }
}
