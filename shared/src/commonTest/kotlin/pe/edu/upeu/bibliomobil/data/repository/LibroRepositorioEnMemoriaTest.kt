package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals

class LibroRepositorioEnMemoriaTest {
    @Test fun idsCorrelativosDesdeUno() = runTest {
        val repo = LibroRepositorioEnMemoria()
        assertEquals(1L, repo.registrar(libro("Uno")).id)
        assertEquals(2L, repo.registrar(libro("Dos")).id)
    }

    @Test fun listaEnOrdenDeRegistro() = runTest {
        val repo = LibroRepositorioEnMemoria()
        repo.registrar(libro("Primero")); repo.registrar(libro("Segundo"))
        assertEquals(listOf("Primero", "Segundo"), repo.listar().map { it.titulo })
    }

    private fun libro(titulo: String) = Libro(0, titulo, "Autor", 2000, 1)
}
