package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/** Puerto de la cartera de lectores para registrar y consultar lectores. */
interface LectorRepository {
    suspend fun registrar(lector: Lector): Lector
    suspend fun listar(): List<Lector>
}
