package com.ct.junimostoreapp.login

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.login.RegistroViewModel
import com.ct.junimostoreapp.ui.login.RegistroViewModelFactory
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.ui.theme.JunimoStoreAppTheme
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O) // Anotación que indica que esta función requiere Android 8.0 (API 26) o superior, debido al uso de `java.time`.
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun RegistroScreen( // Define la pantalla de registro de usuarios.
    navController: NavController, // Controlador de navegación para moverse a otras pantallas.
) {
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: RegistroViewModel = viewModel(factory = RegistroViewModelFactory(application.authRepository))
    val state = vm.uiState // Obtiene el estado actual de la UI desde el ViewModel.
    val context = LocalContext.current // Obtiene el contexto actual, necesario para el DatePickerDialog.
    val showDatePicker = remember { mutableStateOf(false) } // Estado para controlar la visibilidad del selector de fecha.
    var showSuccessDialog by remember { mutableStateOf(false) } // Estado para mostrar un diálogo de éxito tras el registro.

    var regionExpanded by remember { mutableStateOf(false) } // Estado para el menú desplegable de regiones.
    var comunaExpanded by remember { mutableStateOf(false) } // Estado para el menú desplegable de comunas.
    var showPassword by remember { mutableStateOf(false) } // Estado para la visibilidad de la contraseña.
    var showConfirmPassword by remember { mutableStateOf(false) } // Estado para la visibilidad de la confirmación de contraseña.

    if (showDatePicker.value) { // Si `showDatePicker` es verdadero, se muestra el diálogo de selección de fecha.
        val calendar = Calendar.getInstance() // Obtiene una instancia del calendario.
        val datePickerDialog = DatePickerDialog( // Crea el diálogo para seleccionar la fecha.
            context, // Contexto de la aplicación.
            R.style.DatePickerTheme, // Tema personalizado para el diálogo.
            { _, year, month, dayOfMonth -> // Lambda que se ejecuta al seleccionar una fecha.
                val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year) // Formatea la fecha seleccionada.
                vm.onFechaNacimientoChange(formattedDate) // Actualiza la fecha en el ViewModel.
            },
            calendar.get(Calendar.YEAR), // Año inicial.
            calendar.get(Calendar.MONTH), // Mes inicial.
            calendar.get(Calendar.DAY_OF_MONTH) // Día inicial.
        )
        datePickerDialog.setOnDismissListener { showDatePicker.value = false } // Oculta el diálogo si se cancela.
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Aceptar", datePickerDialog) // Botón de confirmación.
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancelar", datePickerDialog) // Botón de cancelación.
        datePickerDialog.show() // Muestra el diálogo.
    }

    JunimoStoreAppTheme { // Aplica el tema de la aplicación.
        Scaffold( // Estructura de la pantalla.
            topBar = {
                TopAppBar( // Barra de aplicación superior.
                    title = { Text("Registro de Usuario") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AmarilloMostaza
                    )
                )
            }
        ) { innerPadding ->
            Box( // Contenedor que permite superponer elementos.
                modifier = Modifier
                    .padding(innerPadding) // Aplica el padding de la barra superior.
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Escala la imagen para que llene el contenedor.
                    modifier = Modifier.fillMaxSize()
                )
                Column( // Columna que organiza los campos del formulario.
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()), // Permite el desplazamiento vertical.
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val shadow = Shadow(color = Color.Black.copy(alpha = 0.8f), offset = Offset(2f, 2f), blurRadius = 4f) // Estilo de sombra para el texto.
                    val textStyle = TextStyle(color = Color.White, shadow = shadow) // Estilo de texto para las etiquetas.

                    Image(
                        painter = painterResource(id = R.drawable.registro), // Imagen de cabecera.
                        contentDescription = "Registro",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    val textFieldColors = OutlinedTextFieldDefaults.colors( // Colores personalizados para los campos de texto.
                        unfocusedContainerColor = AmarilloMostaza,
                        focusedContainerColor = AmarilloMostaza,
                        cursorColor = AzulCielo,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )

                    Text("Ingrese su RUN", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para el RUN.
                        value = state.run,
                        onValueChange = vm::onRunChange, // Actualiza el RUN en el ViewModel.
                        label = { Text("RUN") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su nombre", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para el nombre.
                        value = state.nombre,
                        onValueChange = vm::onNombreChange,
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese sus apellidos", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para los apellidos.
                        value = state.apellidos,
                        onValueChange = vm::onApellidosChange,
                        label = { Text("Apellidos") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su correo", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para el correo.
                        value = state.correo,
                        onValueChange = vm::onCorreoChange,
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su contraseña", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para la contraseña.
                        value = state.contrasenha,
                        onValueChange = vm::onContrasenhaChange,
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(), // Oculta o muestra la contraseña.
                        trailingIcon = { // Icono al final del campo.
                            IconButton(onClick = { showPassword = !showPassword }) { // Botón para alternar la visibilidad.
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color.Black
                                )
                            }
                        },
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Confirme su contraseña", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para confirmar la contraseña.
                        value = state.confirmContrasenha,
                        onValueChange = vm::onConfirmContrasenhaChange,
                        label = { Text("Confirmar Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color.Black
                                )
                            }
                        },
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su teléfono (opcional)", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para el teléfono.
                        value = state.telefono,
                        onValueChange = vm::onTelefonoChange,
                        label = { Text("Teléfono (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su fecha de nacimiento", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para la fecha de nacimiento (no editable, abre el diálogo).
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
                        enabled = false // El campo no es editable directamente.
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su región", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox( // Contenedor para el menú desplegable de regiones.
                        expanded = regionExpanded,
                        onExpandedChange = { regionExpanded = !regionExpanded }
                    ) {
                        OutlinedTextField(
                            value = state.region,
                            onValueChange = {},
                            label = { Text("Región") },
                            readOnly = true, // El campo no se puede editar manualmente.
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable), // Ancla el menú al campo de texto.
                            colors = textFieldColors
                        )
                        ExposedDropdownMenu( // El menú desplegable.
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            state.regiones.forEach { region -> // Itera sobre la lista de regiones del ViewModel.
                                DropdownMenuItem( // Cada elemento del menú.
                                    text = { Text(region.nombre) },
                                    onClick = { // Al hacer clic en una región.
                                        vm.onRegionChange(region.nombre) // Actualiza la región en el ViewModel.
                                        regionExpanded = false // Cierra el menú.
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su comuna", style = textStyle)
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
                            colors = textFieldColors,
                            enabled = state.comunas.isNotEmpty() // Habilita el campo solo si hay comunas disponibles.
                        )
                        ExposedDropdownMenu(
                            expanded = comunaExpanded,
                            onDismissRequest = { comunaExpanded = false }
                        ) {
                            state.comunas.forEach { comuna -> // Itera sobre la lista de comunas disponibles.
                                DropdownMenuItem(
                                    text = { Text(comuna) },
                                    onClick = {
                                        vm.onComunaChange(comuna) // Actualiza la comuna en el ViewModel.
                                        comunaExpanded = false // Cierra el menú.
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su dirección", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField( // Campo de texto para la dirección.
                        value = state.direccion,
                        onValueChange = vm::onDireccionChange,
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    if (state.error != null) { // Muestra un mensaje de error si existe en el estado.
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error, 
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button( // Botón para enviar el formulario de registro.
                        onClick = {
                            vm.submit { // Llama a la función de envío del ViewModel.
                                showSuccessDialog = true // Si el registro es exitoso, muestra el diálogo de éxito.
                            }
                        },
                        enabled = !state.isLoading, // Se deshabilita durante la carga.
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulCielo
                        )
                    ) {
                        Text(if (state.isLoading) "Registrando..." else "Registrarse")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row( // Fila para el enlace de inicio de sesión.
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            style = TextStyle(
                                color = Color.White,
                                shadow = shadow
                            )
                        )
                        TextButton(onClick = { navController.popBackStack() }) { // Botón para volver a la pantalla anterior (inicio de sesión).
                            Text(
                                text = "Inicia sesión",
                                style = TextStyle(
                                    color = AmarilloMostaza,
                                    shadow = shadow,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        if (showSuccessDialog) { // Si el registro fue exitoso, muestra este diálogo.
            AlertDialog(
                onDismissRequest = { }, // No se puede cerrar tocando fuera.
                title = { Text("¡Registro exitoso!") },
                text = { Text("Tu cuenta ha sido creada. Ahora puedes iniciar sesión.") },
                confirmButton = { // Botón para confirmar y volver al inicio de sesión.
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack() // Vuelve a la pantalla de inicio de sesión.
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                containerColor = AzulCielo
            )
        }
    }
}