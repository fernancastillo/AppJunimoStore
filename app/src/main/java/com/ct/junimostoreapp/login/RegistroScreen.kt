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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
) {
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: RegistroViewModel = viewModel(factory = RegistroViewModelFactory(application.authRepository))
    val state = vm.uiState
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                vm.onFechaNacimientoChange(formattedDate)
                showDatePicker = false // <<< LA SOLUCIÓN: Cerramos el diálogo aquí
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false } // Aseguramos que se cierre si el usuario toca fuera
            show()
        }
    }

    JunimoStoreAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Registro de Usuario") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AmarilloMostaza
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondostardew),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val shadow = Shadow(color = Color.Black.copy(alpha = 0.8f), offset = Offset(2f, 2f), blurRadius = 4f)
                    val textStyle = TextStyle(color = Color.White, shadow = shadow)

                    Image(
                        painter = painterResource(id = R.drawable.registro),
                        contentDescription = "Registro",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = AmarilloMostaza,
                        focusedContainerColor = AmarilloMostaza,
                        cursorColor = AzulCielo,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    )

                    Text("Ingrese su RUN", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.run,
                        onValueChange = vm::onRunChange,
                        label = { Text("RUN") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su nombre", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = vm::onNombreChange,
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese sus apellidos", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.apellidos,
                        onValueChange = vm::onApellidosChange,
                        label = { Text("Apellidos") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su correo", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = vm::onCorreoChange,
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su contraseña", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.contrasenha,
                        onValueChange = vm::onContrasenhaChange,
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
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
                    OutlinedTextField(
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
                    OutlinedTextField(
                        value = state.telefono,
                        onValueChange = vm::onTelefonoChange,
                        label = { Text("Teléfono (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su fecha de nacimiento", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.fechaNacimiento,
                        onValueChange = {},
                        label = { Text("Fecha de Nacimiento") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
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

                    Text("Ingrese su región", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox(
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
                        ExposedDropdownMenu(
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            state.regiones.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region.nombre) },
                                    onClick = {
                                        vm.onRegionChange(region.nombre)
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su comuna", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox(
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
                            enabled = state.comunas.isNotEmpty()
                        )
                        ExposedDropdownMenu(
                            expanded = comunaExpanded,
                            onDismissRequest = { comunaExpanded = false }
                        ) {
                            state.comunas.forEach { comuna ->
                                DropdownMenuItem(
                                    text = { Text(comuna) },
                                    onClick = {
                                        vm.onComunaChange(comuna)
                                        comunaExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingrese su dirección", style = textStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.direccion,
                        onValueChange = vm::onDireccionChange,
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error, 
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            vm.submit {
                                showSuccessDialog = true
                            }
                        },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulCielo
                        )
                    ) {
                        Text(if (state.isLoading) "Registrando..." else "Registrarse")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
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
                        TextButton(onClick = { navController.popBackStack() }) { 
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

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("¡Registro exitoso!") },
                text = { Text("Tu cuenta ha sido creada. Ahora puedes iniciar sesión.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
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