package com.ct.junimostoreapp.login

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.ct.junimostoreapp.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.ui.login.LoginViewModel
import com.ct.junimostoreapp.ui.login.LoginViewModelFactory
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.ui.theme.JunimoStoreAppTheme
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen( // Define la pantalla de inicio de sesión.
    navController: NavController, // Controlador de navegación para moverse entre pantallas.
    vm: LoginViewModel = viewModel(factory = LoginViewModelFactory((LocalContext.current.applicationContext as ProductoApplication).authRepository))
) {
    val state = vm.uiState // Obtiene el estado de la UI desde el ViewModel.
    var showPass by remember { mutableStateOf(false) } // Estado para controlar la visibilidad de la contraseña.
    var showSuccessDialog by remember { mutableStateOf(false) } // Estado para mostrar un diálogo de éxito al iniciar sesión.
    var navigationInfo by remember { mutableStateOf<Pair<String, String>?>(null) } // Almacena la información de navegación después de un inicio de sesión exitoso.
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular)) // Carga la fuente personalizada.

    val context = LocalContext.current // Obtiene el contexto actual.
    val imageLoader = ImageLoader.Builder(context) // Crea un cargador de imágenes Coil.
        .components {
            if (Build.VERSION.SDK_INT >= 28) { // Comprueba la versión del SDK de Android.
                add(ImageDecoderDecoder.Factory()) // Usa ImageDecoderDecoder para Android 9 (API 28) y superior.
            } else {
                add(GifDecoder.Factory()) // Usa GifDecoder para versiones anteriores de Android.
            }
        }
        .build()

    JunimoStoreAppTheme { // Aplica el tema de la aplicación.
        Scaffold ( // Estructura de la pantalla con soporte para TopAppBar, etc.
            topBar = {
                TopAppBar( // Barra de aplicación superior.
                    title = {Text("Junimo Store App")}, // Título de la barra.
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AmarilloMostaza // Color de fondo de la barra.
                    )
                )
            }
        )
        {
                innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Escala la imagen para que llene el contenedor, recortando si es necesario.
                    modifier = Modifier.fillMaxSize()
                )
                Column ( // Columna que organiza los elementos verticalmente.
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top, // Alinea los elementos en la parte superior.
                    horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente.
                )
                {
                    val shadow = Shadow(color = Color.Black.copy(alpha = 0.8f), offset = Offset(4f, 4f), blurRadius = 8f) // Define un estilo de sombra para el texto.

                    Spacer(modifier = Modifier.height(32.dp)) // Espacio vertical.

                    Image(
                        painter= painterResource(id = R.drawable.junimoshop), // Logo de la tienda.
                        contentDescription = "Logo App",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Fit // Escala la imagen para que se ajuste dentro del contenedor sin recortar.
                    )

                    Spacer(modifier = Modifier.height(64.dp))

                    Text(
                        text = "Ingrese su correo electrónico",
                        style = TextStyle(
                            color = Color.White,
                            shadow = shadow
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val textFieldColors = TextFieldDefaults.colors( // Define los colores para los campos de texto.
                        unfocusedContainerColor = AmarilloMostaza,
                        focusedContainerColor = AmarilloMostaza,
                        cursorColor = AzulCielo, // <<< AQUÍ ESTÁ EL CAMBIO
                        unfocusedIndicatorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )

                    TextField( // Campo de texto para el nombre de usuario (correo).
                        value=state.username,
                        onValueChange = vm::onUsernameChange, // Llama a la función del ViewModel cuando el valor cambia.
                        label = {Text("Correo")},
                        singleLine = true, // El campo de texto solo permite una línea.
                        modifier =Modifier.fillMaxWidth(0.95f),
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingrese su contraseña",
                        style = TextStyle(
                            color = Color.White,
                            shadow = shadow
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    TextField( // Campo de texto para la contraseña.
                        value=state.password,
                        onValueChange = vm::onpasswordChange,
                        label = {Text("Contraseña")},
                        singleLine = true,
                        visualTransformation = if(showPass) VisualTransformation.None else
                            PasswordVisualTransformation(), // Oculta o muestra la contraseña según el estado de `showPass`.

                        trailingIcon = { // Icono al final del campo de texto.
                            TextButton(onClick={showPass =!showPass}) // Botón para cambiar la visibilidad de la contraseña.
                            {
                                Text(if(showPass) "Ocultar" else "Ver", color = Color.Black)
                            }
                        },
                        modifier =Modifier.fillMaxWidth(0.95f),
                        colors = textFieldColors
                    )


                    if (state.error !=null) { // Muestra un mensaje de error si existe.
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text =state.error ?:
                            " ",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { // Botón para iniciar sesión.
                        vm.submit { route, rut -> // Llama a la función de envío del ViewModel.
                            navigationInfo = Pair(route, rut) // Almacena la información de navegación.
                            showSuccessDialog = true // Muestra el diálogo de éxito.
                        }
                    },
                        enabled=!state.isLoading, // El botón está deshabilitado mientras se carga.
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulCielo
                        )

                    )
                    {
                        Text(if(state.isLoading) "Validando" else "Iniciar Sesion"  ) // Texto del botón que cambia según el estado de carga.
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row( // Fila para el texto de registro.
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta?",
                            style = TextStyle(
                                color = Color.White,
                                shadow = shadow
                            )
                        )
                        TextButton(onClick = { navController.navigate("registro") }) { // Botón que navega a la pantalla de registro.
                            Text(
                                text = "Regístrate aquí",
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

        if (showSuccessDialog) { // Diálogo que se muestra al iniciar sesión con éxito.
            LaunchedEffect(navigationInfo) { // Efecto que se ejecuta cuando `navigationInfo` cambia.
                delay(2000) // Espera 2 segundos.
                navigationInfo?.let { // Si la información de navegación no es nula.
                    navController.navigate(it.first) { // Navega a la ruta correspondiente.
                        popUpTo("login") { inclusive = true } // Elimina la pantalla de inicio de sesión del historial de navegación.
                        launchSingleTop = true // Evita que se creen múltiples instancias de la misma pantalla.
                    }
                }
            }

            AlertDialog( // Diálogo de alerta.
                onDismissRequest = { },
                containerColor = AmarilloMostaza,
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Iniciando Sesión",
                            fontFamily = indieFlowerFont,
                            color = Color.Black,
                            fontSize = 28.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.8f),
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AsyncImage( // Imagen animada (GIF).
                            model = R.drawable.login,
                            contentDescription = "Iniciando Sesión",
                            imageLoader = imageLoader,
                            modifier = Modifier.height(150.dp)
                        )
                    }
                },
                confirmButton = {}
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){ // Vista previa de la pantalla de inicio de sesión.
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}