package pe.edu.upeu.bibliomobil.data.repository

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class LibroRepositorioEnMemoria : LibroRepository {
    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        simularLatencia()
        return mutex.withLock {
            libro.copy(id = siguienteId++).also(libros::add)
        }
    }

    override suspend fun listar(): List<Libro> {
        simularLatencia()
        return mutex.withLock { libros.toList() }
    }

    private suspend fun simularLatencia() = delay(Random.nextLong(300L, 801L))
}
