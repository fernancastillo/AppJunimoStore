package com.ct.junimostoreapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopAppBar(navController: NavController, title: String) { // Define la barra de aplicación superior para el dashboard de administrador.
    var showMenu by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del menú desplegable.
    var showConfirmDialog by remember { mutableStateOf(false) } // Estado para el diálogo de confirmación de cierre de sesión.
    val menuItems = listOf("Dashboard", "Productos", "Usuarios", "Ordenes", "Cerrar Sesión") // Lista de elementos del menú.
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower)) // Carga la fuente personalizada para el título.

    if (showConfirmDialog) { // Si se debe mostrar el diálogo de confirmación.
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false }, // Cierra el diálogo si se presiona fuera de él.
            title = { Text("Confirmar Cierre de Sesión") }, // Título del diálogo.
            text = { Text("¿Estás seguro de que quieres cerrar sesión?") }, // Mensaje de confirmación.
            containerColor = AzulCielo, // Color de fondo del diálogo.
            confirmButton = { // Botón para confirmar la acción.
                Button(
                    onClick = {
                        showConfirmDialog = false // Cierra el diálogo.
                        navController.navigate("login") { // Navega a la pantalla de inicio de sesión.
                            popUpTo("login") { inclusive = true } // Limpia el historial de navegación para que el usuario no pueda volver atrás.
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza) // Color del botón.
                ) {
                    Text("Confirmar", color = Color.Black) // Texto del botón.
                }
            },
            dismissButton = { // Botón para cancelar la acción.
                Button(
                    onClick = { showConfirmDialog = false }, // Cierra el diálogo.
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray) // Color del botón.
                ) {
                    Text("Cancelar") // Texto del botón.
                }
            }
        )
    }

    TopAppBar( // Barra de aplicación superior de Material 3.
        title = { Text(text = title, fontFamily = indieFlowerFont) }, // Título de la barra con la fuente personalizada.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AmarilloMostaza // Color de fondo de la barra.
        ),
        actions = { // Acciones que aparecen a la derecha del título.
            Box { // Contenedor para posicionar el menú desplegable.
                IconButton(onClick = { showMenu = !showMenu }) { // Botón de ícono para mostrar/ocultar el menú.
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú") // Ícono de tres puntos verticales.
                }
                DropdownMenu( // Menú que se muestra cuando `showMenu` es verdadero.
                    expanded = showMenu, // El estado de expansión del menú.
                    onDismissRequest = { showMenu = false }, // Cierra el menú si se presiona fuera de él.
                    modifier = Modifier.background(AmarilloMostaza) // Color de fondo del menú.
                ) {
                    menuItems.forEachIndexed { index, item -> // Itera sobre la lista de elementos del menú.
                        Column {
                            DropdownMenuItem( // Cada uno de los elementos del menú.
                                text = { Text(item) }, // Texto del elemento.
                                onClick = { // Acción que se ejecuta al hacer clic.
                                    showMenu = false // Cierra el menú.
                                    when (item) { // Navega a la pantalla correspondiente según el elemento seleccionado.
                                        "Dashboard" -> {
                                            navController.popBackStack("dashboard/{adminName}", inclusive = false)
                                        }
                                        "Productos" -> navController.navigate("admin_productos")
                                        "Usuarios" -> navController.navigate("admin_usuarios")
                                        "Ordenes" -> navController.navigate("admin_ordenes")
                                        "Cerrar Sesión" -> showConfirmDialog = true // Muestra el diálogo de confirmación para cerrar sesión.
                                    }
                                }
                            )
                            if (index < menuItems.lastIndex) { // Si no es el último elemento, añade un divisor.
                                HorizontalDivider(color = Color.Black, thickness = 1.dp) // Línea divisoria entre elementos.
                            }
                        }
                    }
                }
            }
        }
    )
}
