package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/** Puerto del catálogo para registrar libros y consultar su orden de ingreso. */
interface LibroRepository {
    suspend fun registrar(libro: Libro): Libro
    suspend fun listar(): List<Libro>
}
