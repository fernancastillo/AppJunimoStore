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

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun JunimoTopAppBar(navController: NavController, rut: String, title: String) { // Define la barra de aplicación superior personalizada.
    var showMenu by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del menú desplegable.
    var showConfirmDialog by remember { mutableStateOf(false) } // Estado para el diálogo de confirmación de cierre de sesión.
    val menuItems = listOf("Inicio", "Perfil", "Carrito", "Productos", "QR", "Blogs", "Nosotros", "Contacto", "Cerrar Sesión") // Elementos del menú.
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower)) // Carga la fuente personalizada.

    if (showConfirmDialog) { // Si se debe mostrar el diálogo de confirmación.
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Cierre de Sesión") },
            text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
            containerColor = AzulCielo,
            confirmButton = { // Botón para confirmar el cierre de sesión.
                Button(
                    onClick = {
                        showConfirmDialog = false
                        navController.navigate("login") { // Navega a la pantalla de inicio de sesión.
                            popUpTo("login") { inclusive = true } // Limpia el historial de navegación.
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Confirmar", color = Color.Black)
                }
            },
            dismissButton = { // Botón para cancelar el cierre de sesión.
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    TopAppBar( // Barra de aplicación superior.
        title = { Text(text = title, fontFamily = indieFlowerFont) }, // Título de la barra con la fuente personalizada.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AmarilloMostaza // Color de fondo de la barra.
        ),
        actions = { // Acciones a la derecha del título.
            Box { // Contenedor para el menú.
                IconButton(onClick = { showMenu = !showMenu }) { // Botón para mostrar/ocultar el menú.
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                }
                DropdownMenu( // Menú desplegable.
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(AmarilloMostaza)
                ) {
                    menuItems.forEachIndexed { index, item -> // Itera sobre los elementos del menú.
                        Column {
                            DropdownMenuItem( // Elemento del menú.
                                text = { Text(item) },
                                onClick = { // Acción al hacer clic en un elemento.
                                    showMenu = false // Cierra el menú.
                                    when (item) { // Navega a la pantalla correspondiente.
                                        "Inicio" -> navController.navigate("index/$rut")
                                        "Perfil" -> navController.navigate("perfil/$rut")
                                        "Carrito" -> navController.navigate("carrito/$rut")
                                        "Productos" -> navController.navigate("productos/$rut")
                                        "QR" -> navController.navigate("qr/$rut")
                                        "Blogs" -> navController.navigate("blogs/$rut")
                                        "Nosotros" -> navController.navigate("nosotros/$rut")
                                        "Contacto" -> navController.navigate("contacto/$rut")
                                        "Cerrar Sesión" -> showConfirmDialog = true // Muestra el diálogo de confirmación.
                                    }
                                }
                            )
                            if (index < menuItems.lastIndex) { // Añade un divisor entre los elementos.
                                HorizontalDivider(color = Color.Black, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    )
}
