package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class ListarLibrosUseCase(private val repository: LibroRepository) {
    suspend operator fun invoke(): Result<List<Libro>> = resultadoDe { repository.listar() }
}

class ListarLectoresUseCase(private val repository: LectorRepository) {
    suspend operator fun invoke(): Result<List<Lector>> = resultadoDe { repository.listar() }
}
