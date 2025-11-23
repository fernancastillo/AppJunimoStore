package com.ct.junimostoreapp.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.ct.junimostoreapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) { // Define la pantalla de carga (splash screen).
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular)) // Carga la fuente personalizada.
    val context = LocalContext.current // Obtiene el contexto actual.
    val imageLoader = ImageLoader.Builder(context) // Crea un cargador de imágenes Coil.
        .components {
            if (SDK_INT >= 28) { // Comprueba la versión del SDK de Android.
                add(ImageDecoderDecoder.Factory()) // Usa ImageDecoderDecoder para Android 9 (API 28) y superior.
            } else {
                add(GifDecoder.Factory()) // Usa GifDecoder para versiones anteriores de Android.
            }
        }
        .build()

    LaunchedEffect(Unit) { // Efecto que se ejecuta una sola vez cuando el composable entra en la composición.
        delay(3000) // Espera 3 segundos.
        navController.navigate("login") { // Navega a la pantalla de inicio de sesión.
            popUpTo("splash") { inclusive = true } // Elimina la pantalla de carga del historial de navegación.
        }
    }

    Box(modifier = Modifier.fillMaxSize()) { // Contenedor que ocupa toda la pantalla.
        Image( // Imagen de fondo.
            painter = painterResource(id = R.drawable.fondostardew),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Escala la imagen para que llene el contenedor.
            modifier = Modifier.fillMaxSize()
        )

        // Contenido centrado
        Column( // Columna que centra el contenido vertical y horizontalmente.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage( // Muestra una imagen animada (GIF).
                model = R.drawable.cargando,
                contentDescription = "Cargando",
                imageLoader = imageLoader,
                modifier = Modifier.height(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( // Texto "Cargando".
                text = "Cargando",
                fontFamily = indieFlowerFont,
                color = Color.White,
                fontSize = 40.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
        }

        // Imagen inferior
        Box( // Contenedor para la imagen inferior.
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter // Alinea la imagen en la parte inferior central.
        ) {
            Image(
                painter = painterResource(id = R.drawable.developed_stardew), // Imagen de créditos.
                contentDescription = "Developed by ConcernedApe",
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}