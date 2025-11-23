package com.ct.junimostoreapp.view

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.EditarPerfilViewModel
import com.ct.junimostoreapp.viewmodel.EditarPerfilViewModelFactory
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O) // Anotación que indica que esta función requiere Android 8.0 (API 26) o superior.
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun EditarPerfilScreen( // Define la pantalla para editar el perfil de usuario.
    navController: NavController, // Controlador de navegación para moverse a otras pantallas.
    rut: String // RUN del usuario a editar.
) {
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: EditarPerfilViewModel = viewModel(factory = EditarPerfilViewModelFactory(application.authRepository))
    LaunchedEffect(rut) { // Efecto que se ejecuta cuando el RUT cambia.
        vm.loadUserProfile(rut) // Carga los datos del perfil del usuario.
    }
    val state = vm.uiState // Obtiene el estado de la UI desde el ViewModel.
    val context = LocalContext.current // Obtiene el contexto actual.
    val showDatePicker = remember { mutableStateOf(false) } // Estado para el selector de fecha.

    var regionExpanded by remember { mutableStateOf(false) } // Estado para el menú de regiones.
    var comunaExpanded by remember { mutableStateOf(false) } // Estado para el menú de comunas.
    var showPassword by remember { mutableStateOf(false) } // Estado para la visibilidad de la contraseña.
    var showConfirmPassword by remember { mutableStateOf(false) } // Estado para la visibilidad de la confirmación de contraseña.

    if (state.successMessage != null) { // Si hay un mensaje de éxito, muestra un diálogo.
        AlertDialog(
            onDismissRequest = { vm.dismissSuccessMessage() },
            title = { Text("¡Perfil editado con éxito!") },
            containerColor = AmarilloMostaza,
            confirmButton = { // Botón para confirmar y volver a la pantalla anterior.
                Button(
                    onClick = {
                        vm.dismissSuccessMessage()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                ) {
                    Text("Aceptar", color = Color.Black)
                }
            }
        )
    }

    if (showDatePicker.value) { // Si se debe mostrar el selector de fecha.
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog( // Crea el diálogo para seleccionar la fecha.
            context,
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                vm.onFechaNacimientoChange(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker.value = false }
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Aceptar", datePickerDialog)
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancelar", datePickerDialog)
        datePickerDialog.show()
    }

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            TopAppBar( // Barra de aplicación superior.
                title = { Text("Editar Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AmarilloMostaza
                )
            )
        }
    ) { innerPadding ->
        Box( // Contenedor que permite superponer elementos.
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column( // Columna para el formulario de edición.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val shadow = Shadow(color = Color.Black.copy(alpha = 0.8f), offset = Offset(2f, 2f), blurRadius = 4f)
                val textStyle = TextStyle(color = Color.White, shadow = shadow)
                val textFieldColors = OutlinedTextFieldDefaults.colors( // Colores para los campos de texto.
                    unfocusedContainerColor = AmarilloMostaza,
                    focusedContainerColor = AmarilloMostaza,
                    cursorColor = AzulCielo,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )

                Text("RUN: ${state.run}", style = textStyle.copy(fontWeight = FontWeight.Bold)) // Muestra el RUN del usuario (no editable).
                Spacer(modifier = Modifier.height(8.dp))

                Text("Nombre", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para el nombre.
                    value = state.nombre,
                    onValueChange = vm::onNombreChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Apellidos", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para los apellidos.
                    value = state.apellidos,
                    onValueChange = vm::onApellidosChange,
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Correo", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para el correo.
                    value = state.correo,
                    onValueChange = vm::onCorreoChange,
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Nueva Contraseña (opcional)", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para la nueva contraseña.
                    value = state.contrasenha,
                    onValueChange = vm::onContrasenhaChange,
                    label = { Text("Nueva Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { // Icono para mostrar/ocultar la contraseña.
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Confirmar Nueva Contraseña", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo para confirmar la nueva contraseña.
                    value = state.confirmContrasenha,
                    onValueChange = vm::onConfirmContrasenhaChange,
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { // Icono para mostrar/ocultar la confirmación.
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showConfirmPassword) "Ocultar" else "Mostrar",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Teléfono", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para el teléfono.
                    value = state.telefono,
                    onValueChange = vm::onTelefonoChange,
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Fecha de Nacimiento", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo que muestra la fecha de nacimiento y abre el selector de fecha.
                    value = state.fechaNacimiento,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker.value = true }, // Muestra el selector de fecha al hacer clic.
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = AmarilloMostaza,
                        disabledTextColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        disabledTrailingIconColor = Color.Black
                    ),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Región", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox( // Contenedor para el menú desplegable de regiones.
                    expanded = regionExpanded,
                    onExpandedChange = { regionExpanded = !regionExpanded }
                ) {
                    OutlinedTextField(
                        value = state.region,
                        onValueChange = {},
                        label = { Text("Región") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu( // Menú desplegable de regiones.
                        expanded = regionExpanded,
                        onDismissRequest = { regionExpanded = false }
                    ) {
                        state.regiones.forEach { 
                            DropdownMenuItem(
                                text = { Text(it.nombre) },
                                onClick = {
                                    vm.onRegionChange(it.nombre)
                                    regionExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Comuna", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox( // Contenedor para el menú desplegable de comunas.
                    expanded = comunaExpanded,
                    onExpandedChange = { comunaExpanded = !comunaExpanded }
                ) {
                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = {},
                        label = { Text("Comuna") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        enabled = state.comunas.isNotEmpty(),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu( // Menú desplegable de comunas.
                        expanded = comunaExpanded,
                        onDismissRequest = { comunaExpanded = false }
                    ) {
                        state.comunas.forEach { 
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    vm.onComunaChange(it)
                                    comunaExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Dirección", style = textStyle)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField( // Campo de texto para la dirección.
                    value = state.direccion,
                    onValueChange = vm::onDireccionChange,
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                if (state.error != null) { // Muestra un mensaje de error si existe.
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: " ",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button( // Botón para guardar los cambios.
                    onClick = {
                        vm.submit { navController.popBackStack() }
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Text(if (state.isLoading) "Guardando..." else "Guardar Cambios")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button( // Botón para cancelar la edición.
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}