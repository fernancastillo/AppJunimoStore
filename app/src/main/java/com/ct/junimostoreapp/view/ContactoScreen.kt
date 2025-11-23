package com.ct.junimostoreapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.ContactoViewModel

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun ContactoScreen(navController: NavController, rut: String, vm: ContactoViewModel = viewModel()) { // Define la pantalla de contacto.
    val state = vm.uiState // Obtiene el estado de la UI desde el ViewModel.
    var showMenu by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del menú.
    val menuItems = listOf("Inicio", "Perfil", "Productos", "QR", "Blogs", "Nosotros", "Contacto", "Cerrar Sesión") // Elementos del menú.

    if (state.successMessage != null) { // Si hay un mensaje de éxito, muestra un diálogo.
        AlertDialog(
            onDismissRequest = { vm.dismissMessages() }, // Cierra el diálogo y limpia los mensajes.
            title = { Text(state.successMessage) },
            containerColor = AmarilloMostaza,
            confirmButton = { // Botón para confirmar y navegar al inicio.
                Button(
                    onClick = {
                        vm.dismissMessages()
                        navController.navigate("index/$rut") {
                            popUpTo("index/$rut") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                ) {
                    Text("Aceptar", color = Color.Black)
                }
            }
        )
    }

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Contacto") // Barra de aplicación superior.
        }
    ) { innerPadding ->
        Box( // Contenedor que permite superponer elementos.
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column( // Columna para el formulario de contacto.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.contacto), // Imagen de cabecera de la sección.
                    contentDescription = "Contacto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))

                val textFieldColors = OutlinedTextFieldDefaults.colors( // Colores para los campos de texto.
                    unfocusedContainerColor = AmarilloMostaza,
                    focusedContainerColor = AmarilloMostaza,
                    cursorColor = AzulCielo,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )

                OutlinedTextField( // Campo de texto para el nombre.
                    value = state.name,
                    onValueChange = vm::onNameChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField( // Campo de texto para el correo.
                    value = state.email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField( // Campo de texto para el asunto.
                    value = state.asunto,
                    onValueChange = vm::onAsuntoChange,
                    label = { Text("Asunto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField( // Campo de texto para el mensaje.
                    value = state.message,
                    onValueChange = vm::onMessageChange,
                    label = { Text("Mensaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = textFieldColors
                )

                state.error?.let { // Muestra un mensaje de error si existe.
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button( // Botón para enviar el formulario.
                    onClick = { vm.submit() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}