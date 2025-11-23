package com.ct.junimostoreapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun MuestraDatosScreen( // Define una pantalla simple para mostrar un mensaje de bienvenida y un botón de cierre de sesión.
    username: String, // Nombre de usuario a mostrar.
    navController: NavController // Controlador de navegación para manejar las acciones de navegación.
) {
    Scaffold( // Estructura de la pantalla con soporte para TopAppBar.
        topBar = {
            TopAppBar( // Barra de aplicación superior.
                title = { Text("Muestra Datos") } // Título de la barra.
            )
        }
    ) { innerPadding ->
        Column( // Columna que organiza los elementos verticalmente.
            modifier = Modifier
                .padding(innerPadding) // Aplica el padding de la barra superior.
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Añade espacio entre los elementos de la columna.
        ) {
            Text( // Muestra el mensaje de bienvenida.
                text = "Bienvenido, $username",
                style = MaterialTheme.typography.headlineMedium // Aplica un estilo de tipografía predefinido.
            )

            Button( // Botón para cerrar la sesión.
                onClick = { // Acción que se ejecuta al hacer clic en el botón.
                    navController.navigate("login") { // Navega a la pantalla de inicio de sesión.
                        popUpTo("login") { inclusive = true } // Elimina todas las pantallas del historial hasta la de login.
                        launchSingleTop = true // Evita que se creen múltiples instancias de la pantalla de login.
                    }
                }
            ) {


                Text("Cerrar sesión")
            }
        }
    }
}