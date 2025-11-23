package com.ct.junimostoreapp.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.view.JunimoTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(navController: NavController, rut: String) {
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular))

    // --- INICIO DE LA ANIMACIÓN DE RESPIRACIÓN ---

    // 1. Se crea una transición infinita. Esta se encargará de gestionar y ejecutar la animación en un bucle continuo sin fin.
    val infiniteTransition = rememberInfiniteTransition(label = "breathing_animation")

    // 2. Se define el valor que se animará. En este caso, es la "escala" (el tamaño) del componente.
    val scale by infiniteTransition.animateFloat(
        // Valor inicial de la escala (1.0f significa tamaño normal, sin alteraciones).
        initialValue = 1f,
        // Valor final de la escala (1.02f significa que crecerá hasta un 2% de su tamaño original).
        targetValue = 1.02f,
        // 3. Se especifica cómo se debe comportar la animación.
        animationSpec = infiniteRepeatable(
            // `tween` define la duración y la "curva" de la animación. 1500 milisegundos (1.5s) para una transición suave de un estado a otro.
            animation = tween(1500),
            // `RepeatMode.Reverse` hace que la animación vaya de inicio a fin (1f a 1.02f) y luego de fin a inicio (1.02f a 1f).
            // Esto crea el efecto continuo de "inhalar" y "exhalar".
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    // --- FIN DE LA ANIMACIÓN ---

    Scaffold(
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Junimo Store")
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
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.junimoshop),
                    contentDescription = "Junimo Store",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    // 4. Se aplica la escala animada a la Card. Usamos `graphicsLayer` porque es una forma muy eficiente
                    // de aplicar transformaciones visuales (escala, rotación, etc.) sin afectar el layout general.
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale // Aplica la animación al eje horizontal.
                            scaleY = scale // Aplica la animación al eje vertical.
                        },
                    colors = CardDefaults.cardColors(containerColor = AzulCielo)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "¡Bienvenido a Junimo Store!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = indieFlowerFont,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.junimoss),
                            contentDescription = "Junimos",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tu tienda de confianza para todo lo relacionado con Stardew Valley. Aquí encontrarás productos de alta calidad, desde peluches hasta guías ilustradas, para que puedas llevar un pedacito del valle a tu hogar.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}