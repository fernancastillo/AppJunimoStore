package com.ct.junimostoreapp.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.AdminUsuariosViewModel
import com.ct.junimostoreapp.viewmodel.AdminUsuariosViewModelFactory
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminUsuariosScreen(navController: NavController) { // Pantalla para la gestión de usuarios.
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: AdminUsuariosViewModel = viewModel(factory = AdminUsuariosViewModelFactory(application.authRepository))
    val usuarios by vm.usuarios.collectAsState() // Obtiene la lista de usuarios desde el ViewModel y la observa como un estado de Compose.

    Scaffold(
        topBar = {
            DashboardTopAppBar(navController = navController, title = "Gestión de Usuarios") // Barra de aplicación superior del dashboard.
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding) // Aplica el padding interno proporcionado por el Scaffold.
                .fillMaxSize(), // Rellena todo el tamaño disponible.
            contentAlignment = Alignment.Center // Centra el contenido dentro del Box.
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew), // Establece la imagen de fondo.
                contentDescription = null, // Descripción nula para imágenes decorativas.
                contentScale = ContentScale.Crop, // Escala la imagen para que llene el contenedor, recortando si es necesario.
                modifier = Modifier.fillMaxSize() // La imagen de fondo ocupa todo el espacio.
            )
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) { // Columna principal que contiene todos los elementos de la pantalla.
                Image(
                    painter = painterResource(id = R.drawable.usuarios), // Muestra el ícono de usuarios.
                    contentDescription = "Usuarios", // Descripción para accesibilidad.
                    modifier = Modifier
                        .fillMaxWidth() // La imagen ocupa todo el ancho.
                        .height(150.dp), // Altura fija para la imagen.
                    contentScale = ContentScale.Fit // Escala la imagen para que se ajuste sin recortar.
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.
                Button(
                    onClick = { vm.onAddUserClick() }, // Acción al hacer clic: abre el diálogo para agregar un nuevo usuario.
                    modifier = Modifier.fillMaxWidth(), // El botón ocupa todo el ancho.
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)) // Color verde oscuro para el botón.
                ) {
                    Text("Agregar Usuario", color = Color.White) // Texto del botón.
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.
                LazyColumn { // Lista perezosa que solo renderiza los elementos visibles en la pantalla.
                    items(usuarios) { usuario -> // Itera sobre la lista de usuarios.
                        Card( // Tarjeta para mostrar la información de cada usuario.
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp), // Espacio vertical entre tarjetas.
                            colors = CardDefaults.cardColors(containerColor = AzulCielo) // Color de fondo de la tarjeta.
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) { // Contenido de la tarjeta.
                                Text(text = "RUN: ${usuario.run}", fontWeight = FontWeight.Bold) // Muestra el RUN del usuario.
                                Text(text = "Nombre: ${usuario.nombre} ${usuario.apellidos}") // Muestra el nombre completo del usuario.
                                Text(text = "Correo: ${usuario.correo}") // Muestra el correo del usuario.
                                Text(text = "Tipo: ${usuario.tipo}") // Muestra el tipo de usuario (Admin/Cliente).
                                Spacer(modifier = Modifier.padding(8.dp)) // Espacio vertical.
                                Row( // Fila para los botones de acción.
                                    modifier = Modifier.fillMaxWidth(), // La fila ocupa todo el ancho.
                                    horizontalArrangement = Arrangement.End // Alinea los botones al final (derecha).
                                ) {
                                    Button(
                                        onClick = { vm.onEditUserClick(usuario) }, // Acción al hacer clic: abre el diálogo de edición.
                                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza) // Color del botón.
                                    ) {
                                        Text("Editar", color = Color.Black) // Texto del botón.
                                    }
                                    Spacer(modifier = Modifier.width(8.dp)) // Espacio horizontal entre botones.
                                    Button(
                                        onClick = { vm.onEliminarClick(usuario) }, // Acción al hacer clic: inicia el proceso de eliminación.
                                        enabled = usuario.tipo != "Admin", // El botón se deshabilita si el usuario es de tipo "Admin".
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Color del botón.
                                    ) {
                                        Text("Eliminar", color = Color.White) // Texto del botón.
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (vm.showAddUserDialog) { // Muestra el diálogo para agregar usuario si la variable de estado es verdadera.
            AddUserDialog(vm = vm) // Llama al composable del diálogo de agregación.
        }

        if (vm.showEditUserDialog) { // Muestra el diálogo para editar usuario si la variable de estado es verdadera.
            EditUserDialog(vm = vm) // Llama al composable del diálogo de edición.
        }

        if (vm.showConfirmDialog) { // Muestra el diálogo de confirmación de eliminación.
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() }, // Cierra el diálogo si se presiona fuera de él.
                title = { Text("Confirmar Eliminación") }, // Título del diálogo.
                text = { // Contenido del diálogo.
                    Column {
                        Text("¿Seguro que desea eliminar este usuario?")
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(text = "RUN: ${vm.selectedUsuario?.run}", fontWeight = FontWeight.Bold) // Muestra el RUN del usuario a eliminar.
                        Text(text = "Nombre: ${vm.selectedUsuario?.nombre} ${vm.selectedUsuario?.apellidos}") // Muestra el nombre del usuario a eliminar.
                    }
                },
                confirmButton = { // Botón para confirmar la acción.
                    Button(
                        onClick = { vm.onConfirmEliminar() }, // Llama a la función de confirmación en el ViewModel.
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                dismissButton = { // Botón para cancelar la acción.
                    Button(
                        onClick = { vm.onDialogDismiss() }, // Cierra el diálogo.
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancelar")
                    }
                },
                containerColor = AzulCielo // Color de fondo del diálogo.
            )
        }

        if (vm.showSuccessDialog) { // Muestra el diálogo de éxito tras la eliminación.
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() }, // Cierra el diálogo.
                title = { Text("Usuario Eliminado") },
                text = { Text("El usuario ha sido eliminado con éxito.") },
                confirmButton = { // Botón de confirmación.
                    Button(
                        onClick = { vm.onDialogDismiss() }, // Cierra el diálogo.
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                containerColor = AzulCielo // Color de fondo del diálogo.
            )
        }

        if (vm.showAddUserSuccessDialog) { // Muestra un diálogo de éxito si se agregó un usuario.
            AlertDialog(
                onDismissRequest = { vm.onAddUserSuccessDialogDismiss() }, // Cierra el diálogo.
                title = {
                    Text(
                        text = "Usuario agregado con éxito",
                        fontFamily = FontFamily(Font(R.font.indie_flower)), // Fuente personalizada.
                        color = Color.Black,
                        textAlign = TextAlign.Center, // Centra el texto del título.
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = { // Botón de confirmación.
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { // Centra el botón.
                        Button(
                            onClick = { vm.onAddUserSuccessDialogDismiss() }, // Cierra el diálogo al hacer clic.
                            colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                        ) {
                            Text("Aceptar", color = Color.Black)
                        }
                    }
                },
                containerColor = AmarilloMostaza // Color de fondo del diálogo.
            )
        }

        if (vm.showEditUserSuccessDialog) { // Muestra un diálogo de éxito si se editó un usuario.
            AlertDialog(
                onDismissRequest = { vm.onEditUserSuccessDialogDismiss() }, // Cierra el diálogo.
                title = {
                    Text(
                        text = "Usuario actualizado con éxito",
                        fontFamily = FontFamily(Font(R.font.indie_flower)), // Fuente personalizada.
                        color = Color.Black,
                        textAlign = TextAlign.Center, // Centra el texto del título.
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = { // Botón de confirmación.
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { // Centra el botón.
                        Button(
                            onClick = { vm.onEditUserSuccessDialogDismiss() }, // Cierra el diálogo al hacer clic.
                            colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                        ) {
                            Text("Aceptar", color = Color.Black)
                        }
                    }
                },
                containerColor = AmarilloMostaza // Color de fondo del diálogo.
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(vm: AdminUsuariosViewModel) { // Composable para el diálogo de agregar un nuevo usuario.
    val state = vm.registroUiState // Obtiene el estado del formulario de registro desde el ViewModel.
    val context = LocalContext.current // Obtiene el contexto actual (necesario para el DatePickerDialog).
    val showDatePicker = remember { mutableStateOf(false) } // Estado para controlar la visibilidad del selector de fecha.
    var tipoExpanded by remember { mutableStateOf(false) } // Estado para el menú desplegable de tipo de usuario.
    var regionExpanded by remember { mutableStateOf(false) } // Estado para el menú desplegable de región.
    var comunaExpanded by remember { mutableStateOf(false) } // Estado para el menú desplegable de comuna.
    val tipos = listOf("Admin", "Cliente") // Lista de tipos de usuario.

    if (showDatePicker.value) { // Si se debe mostrar el selector de fecha.
        val calendar = Calendar.getInstance() // Obtiene una instancia del calendario.
        val datePickerDialog = DatePickerDialog( // Crea el diálogo de selección de fecha.
            context,
            R.style.DatePickerTheme, // Tema personalizado para el DatePickerDialog.
            { _, year, month, dayOfMonth -> // Callback que se ejecuta al seleccionar una fecha.
                val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year) // Formatea la fecha seleccionada.
                vm.onFechaNacimientoChange(formattedDate) // Actualiza la fecha de nacimiento en el ViewModel.
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker.value = false } // Cierra el selector si se presiona fuera.
        datePickerDialog.show() // Muestra el diálogo.
    }

    AlertDialog(
        onDismissRequest = { vm.onAddUserDismiss() }, // Cierra el diálogo al presionar fuera.
        title = { Text("Agregar Nuevo Usuario", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }, // Título del diálogo.
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // Columna que permite el desplazamiento vertical.
                val textFieldColors = OutlinedTextFieldDefaults.colors( // Define los colores para los campos de texto.
                    unfocusedContainerColor = AzulCielo,
                    focusedContainerColor = AzulCielo,
                    disabledContainerColor = AzulCielo,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    disabledTrailingIconColor = Color.Black
                )
                OutlinedTextField(value = state.run, onValueChange = vm::onRunChange, label = { Text("RUN") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.nombre, onValueChange = vm::onNombreChange, label = { Text("Nombre") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.apellidos, onValueChange = vm::onApellidosChange, label = { Text("Apellidos") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.correo, onValueChange = vm::onCorreoChange, label = { Text("Correo") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.contrasenha, onValueChange = vm::onContrasenhaChange, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), colors = textFieldColors, modifier = Modifier.fillMaxWidth()) // Campo de contraseña con transformación visual.
                OutlinedTextField(value = state.confirmContrasenha, onValueChange = vm::onConfirmContrasenhaChange, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.telefono, onValueChange = vm::onTelefonoChange, label = { Text("Teléfono") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = state.fechaNacimiento,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker.value = true }, // Al hacer clic, muestra el selector de fecha.
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }, // Ícono de calendario.
                    enabled = false, // El campo no es editable directamente.
                    colors = textFieldColors
                )
                ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox para Región.
                    OutlinedTextField(
                        value = state.region,
                        onValueChange = {},
                        label = { Text("Región") },
                        readOnly = true, // Solo lectura.
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                        state.regiones.forEach { region ->
                            DropdownMenuItem(text = { Text(region.nombre) }, onClick = { // Acción al seleccionar una región.
                                vm.onRegionChange(region.nombre) // Actualiza la región en el ViewModel.
                                regionExpanded = false // Cierra el menú.
                            })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = comunaExpanded, onExpandedChange = { comunaExpanded = !comunaExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox para Comuna.
                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = {},
                        label = { Text("Comuna") },
                        readOnly = true, // Solo lectura.
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        enabled = state.region.isNotEmpty(), // Habilitado solo si se ha seleccionado una región.
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                        state.comunas.forEach { comuna ->
                            DropdownMenuItem(text = { Text(comuna) }, onClick = { // Acción al seleccionar una comuna.
                                vm.onComunaChange(comuna) // Actualiza la comuna en el ViewModel.
                                comunaExpanded = false // Cierra el menú.
                            })
                        }
                    }
                }
                OutlinedTextField(value = state.direccion, onValueChange = vm::onDireccionChange, label = { Text("Dirección") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())

                ExposedDropdownMenuBox(expanded = tipoExpanded, onExpandedChange = { tipoExpanded = !tipoExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox para Tipo de Usuario.
                    OutlinedTextField(
                        value = state.tipo,
                        onValueChange = {},
                        label = { Text("Tipo") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = tipoExpanded, onDismissRequest = { tipoExpanded = false }) {
                        tipos.forEach { tipo ->
                            DropdownMenuItem(text = { Text(tipo) }, onClick = { // Acción al seleccionar un tipo.
                                vm.onTipoChange(tipo) // Actualiza el tipo en el ViewModel.
                                tipoExpanded = false // Cierra el menú.
                            })
                        }
                    }
                }

                if (state.error != null) { // Muestra un mensaje de error si existe.
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = { // Botón para agregar el usuario.
            Button(onClick = { vm.submit() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Agregar") }
        },
        dismissButton = { // Botón para cancelar.
            Button(onClick = { vm.onAddUserDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Cancelar") }
        },
        containerColor = AmarilloMostaza // Color de fondo del diálogo.
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDialog(vm: AdminUsuariosViewModel) { // Composable para el diálogo de editar un usuario.
    val state = vm.editUiState // Obtiene el estado del formulario de edición del ViewModel.
    val context = LocalContext.current // Contexto actual.
    val showDatePicker = remember { mutableStateOf(false) } // Estado para el selector de fecha.
    var tipoExpanded by remember { mutableStateOf(false) } // Estado para el combobox de tipo.
    var regionExpanded by remember { mutableStateOf(false) } // Estado para el combobox de región.
    var comunaExpanded by remember { mutableStateOf(false) } // Estado para el combobox de comuna.
    val tipos = listOf("Admin", "Cliente") // Lista de tipos de usuario.

    if (showDatePicker.value) { // Muestra el selector de fecha si es necesario.
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year) // Formatea la fecha.
                vm.onEditFechaNacimientoChange(formattedDate) // Actualiza la fecha en el ViewModel.
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker.value = false }
        datePickerDialog.show()
    }

    AlertDialog(
        onDismissRequest = { vm.onEditUserDismiss() }, // Cierra el diálogo.
        title = { Text("Editar Usuario", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }, // Título.
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // Columna con scroll.
                val textFieldColors = OutlinedTextFieldDefaults.colors( // Colores para los campos de texto.
                    unfocusedContainerColor = AzulCielo,
                    focusedContainerColor = AzulCielo,
                    disabledContainerColor = AzulCielo,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    disabledTrailingIconColor = Color.Black
                )
                OutlinedTextField(value = state.nombre, onValueChange = vm::onEditNombreChange, label = { Text("Nombre") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.apellidos, onValueChange = vm::onEditApellidosChange, label = { Text("Apellidos") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.correo, onValueChange = vm::onEditCorreoChange, label = { Text("Correo") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.contrasenha, onValueChange = vm::onEditContrasenhaChange, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.confirmContrasenha, onValueChange = vm::onEditConfirmContrasenhaChange, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.telefono, onValueChange = vm::onEditTelefonoChange, label = { Text("Teléfono") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = state.fechaNacimiento,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker.value = true }, // Muestra el selector de fecha al hacer clic.
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    enabled = false, // Campo no editable directamente.
                    colors = textFieldColors
                )
                ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox de Región.
                    OutlinedTextField(
                        value = state.region,
                        onValueChange = {},
                        label = { Text("Región") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                        state.regiones.forEach { region ->
                            DropdownMenuItem(text = { Text(region.nombre) }, onClick = { // Acción al seleccionar.
                                vm.onEditRegionChange(region.nombre) // Actualiza en ViewModel.
                                regionExpanded = false // Cierra menú.
                            })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = comunaExpanded, onExpandedChange = { comunaExpanded = !comunaExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox de Comuna.
                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = {},
                        label = { Text("Comuna") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        enabled = state.region.isNotEmpty(), // Habilitado si hay región.
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                        state.comunas.forEach { comuna ->
                            DropdownMenuItem(text = { Text(comuna) }, onClick = { // Acción al seleccionar.
                                vm.onEditComunaChange(comuna) // Actualiza en ViewModel.
                                comunaExpanded = false // Cierra menú.
                            })
                        }
                    }
                }
                OutlinedTextField(value = state.direccion, onValueChange = vm::onEditDireccionChange, label = { Text("Dirección") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())

                ExposedDropdownMenuBox(expanded = tipoExpanded, onExpandedChange = { tipoExpanded = !tipoExpanded }, modifier = Modifier.fillMaxWidth()) { // Combobox de Tipo de Usuario.
                    OutlinedTextField(
                        value = state.tipo,
                        onValueChange = {},
                        label = { Text("Tipo") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = tipoExpanded, onDismissRequest = { tipoExpanded = false }) {
                        tipos.forEach { tipo ->
                            DropdownMenuItem(text = { Text(tipo) }, onClick = { // Acción al seleccionar.
                                vm.onEditTipoChange(tipo) // Actualiza en ViewModel.
                                tipoExpanded = false // Cierra menú.
                            })
                        }
                    }
                }

                if (state.error != null) { // Muestra error si existe.
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = { // Botón para guardar.
            Button(onClick = { vm.submitEdit() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Guardar Cambios") }
        },
        dismissButton = { // Botón para cancelar.
            Button(onClick = { vm.onEditUserDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Cancelar") }
        },
        containerColor = AmarilloMostaza // Color de fondo.
    )
}