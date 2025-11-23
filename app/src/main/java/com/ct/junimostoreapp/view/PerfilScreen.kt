package com.ct.junimostoreapp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.PerfilViewModel
import com.ct.junimostoreapp.viewmodel.PerfilViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O) // Anotación que indica que esta función requiere Android 8.0 (API 26) o superior.
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun PerfilScreen(navController: NavController, rut: String) { // Define la pantalla de perfil de usuario.
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: PerfilViewModel = viewModel(factory = PerfilViewModelFactory(application.authRepository))
    LaunchedEffect(rut) { // Efecto que se ejecuta cuando el RUT cambia.
        vm.loadUserProfile(rut) // Carga los datos del perfil del usuario.
    }
    val state = vm.uiState // Obtiene el estado de la UI desde el ViewModel.

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Mi Perfil") // Barra de aplicación superior.
        }
    ) { innerPadding ->
        Box( // Contenedor que permite superponer elementos.
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column( // Columna que organiza el contenido de la pantalla.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.perfil), // Imagen de cabecera de la sección.
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))

                state.usuario?.let { // Si los datos del usuario están disponibles, muestra la tarjeta de perfil.
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = AzulCielo
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) { // Contenido de la tarjeta de perfil.
                            Text("RUN: ${it.run}", fontWeight = FontWeight.Bold)
                            Text("Nombre: ${it.nombre} ${it.apellidos}")
                            Text("Correo: ${it.correo}")
                            it.telefono?.let { telefono -> Text("Teléfono: $telefono") }
                            it.fechaNacimiento?.let { fecha ->
                                val formattedDate = try { // Formatea la fecha de nacimiento.
                                    LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                } catch (e: Exception) {
                                    fecha // Si falla el formato, muestra la fecha original.
                                }
                                Text("Fecha de Nacimiento: $formattedDate")
                            }
                            it.region?.let { region -> Text("Región: $region") }
                            it.comuna?.let { comuna -> Text("Comuna: $comuna") }
                            it.direccion?.let { direccion -> Text("Dirección: $direccion") }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button( // Botón para navegar a la pantalla de edición de perfil.
                    onClick = { navController.navigate("editar_perfil/$rut") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Editar Perfil", color = Color.Black)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button( // Botón para navegar a la pantalla de historial de pedidos.
                    onClick = { navController.navigate("pedidos/$rut") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Ver Pedidos", color = Color.Black)
                }
            }
        }
    }
}