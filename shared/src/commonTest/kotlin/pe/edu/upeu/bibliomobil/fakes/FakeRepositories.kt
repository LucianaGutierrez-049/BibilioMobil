package pe.edu.upeu.bibliomobil.fakes

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository(librosIniciales: List<Libro> = emptyList()) : LibroRepository {
    private val libros = librosIniciales.toMutableList()
    var fallarAlRegistrar = false
    var fallarAlListar = false
    var registros = 0
    var ultimoRecibido: Libro? = null

    override suspend fun registrar(libro: Libro): Libro {
        if (fallarAlRegistrar) error("Fallo de registro")
        registros++
        ultimoRecibido = libro
        return libro.copy(id = (libros.maxOfOrNull { it.id } ?: 0L) + 1L).also(libros::add)
    }

    override suspend fun listar(): List<Libro> {
        if (fallarAlListar) error("Fallo de listado")
        return libros.toList()
    }
}

class FakeLectorRepository(lectoresIniciales: List<Lector> = emptyList()) : LectorRepository {
    private val lectores = lectoresIniciales.toMutableList()
    var fallarAlRegistrar = false
    var fallarAlListar = false
    var ultimoRecibido: Lector? = null

    override suspend fun registrar(lector: Lector): Lector {
        if (fallarAlRegistrar) error("Fallo de registro")
        ultimoRecibido = lector
        return lector.copy(id = (lectores.maxOfOrNull { it.id } ?: 0L) + 1L).also(lectores::add)
    }

    override suspend fun listar(): List<Lector> {
        if (fallarAlListar) error("Fallo de listado")
        return lectores.toList()
    }
}
