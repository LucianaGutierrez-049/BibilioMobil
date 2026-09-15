package pe.edu.upeu.bibliomobil.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val clave: String) {
    data object Inicio : Screen("inicio")
    data object Libros : Screen("libros")
    data object Lectores : Screen("lectores")
    data object Prestamos : Screen("prestamos")
}

data class Destino(val screen: Screen, val titulo: String, val icono: ImageVector)

val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Libros, "Libros", Icons.Default.MenuBook),
    Destino(Screen.Lectores, "Lectores", Icons.Default.LocalLibrary),
    Destino(Screen.Prestamos, "Préstamos", Icons.Default.Bookmark)
)

fun screenDesdeClave(clave: String): Screen = DESTINOS.firstOrNull { it.screen.clave == clave }?.screen ?: Screen.Inicio
