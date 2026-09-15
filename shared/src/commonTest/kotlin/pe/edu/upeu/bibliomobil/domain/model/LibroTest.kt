package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {
    @Test fun rechazaTituloVacio() { assertFailsWith<IllegalArgumentException> { libro(titulo = " ") } }
    @Test fun rechazaAnioFueraDeRango() { assertFailsWith<IllegalArgumentException> { libro(anio = 1449) } }
    @Test fun requiereReposicionConDos() { assertTrue(libro(ejemplares = 2).requiereReposicion) }
    @Test fun noRequiereReposicionConTres() { assertFalse(libro(ejemplares = 3).requiereReposicion) }

    private fun libro(titulo: String = "El principito", anio: Int = 1943, ejemplares: Int = 3) =
        Libro(1, titulo, "Antoine de Saint-Exupéry", anio, ejemplares)
}
