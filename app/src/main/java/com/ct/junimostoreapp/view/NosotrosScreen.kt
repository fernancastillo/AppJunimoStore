package com.ct.junimostoreapp.view

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun NosotrosScreen(navController: NavController, rut: String) { // Define la pantalla "Sobre Nosotros".
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular)) // Carga la fuente personalizada.

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Nosotros") // Barra de aplicación superior.
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Permite el desplazamiento vertical.
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nosotros), // Imagen de cabecera de la sección.
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para la sección "Nuestra Historia".
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.junimoshop),
                            contentDescription = "Nuestra Historia",
                            modifier = Modifier.height(100.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Nuestra Historia",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Junimos Store nació del amor compartido por Stardew Valley, ese mágico lugar donde podemos escapar de la rutina y construir nuestra granja soñada. Como verdaderos fans del juego, entendemos la magia que envuelve cada detalle del valle y queremos llevar esa experiencia a tu vida cotidiana.\n\nSomos una iniciativa de fanáticos para fanáticos, creada por jugadores apasionados que queremos compartir nuestro entusiasmo por este maravilloso mundo. Aunque no tenemos una ubicación física, nuestro corazón está en cada producto que enviamos a lo largo de todo Chile.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para la sección "Misión".
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.junimo),
                            contentDescription = "Misión",
                            modifier = Modifier.height(100.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Misión",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ofrecer a la comunidad de Stardew Valley en Chile productos de alta calidad que capturen la esencia y la magia del juego, creando una experiencia de compra única y cercana.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para la sección "Visión".
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.gallina),
                            contentDescription = "Visión",
                            modifier = Modifier.height(100.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Visión",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ser la tienda online líder en Chile para la comunidad de Stardew Valley, reconocida por su cercanía, originalidad, y programas de fidelización que premien a los jugadores más comprometidos.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para la sección "Nuestros Valores".
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nuestros Valores",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.caffe),
                            contentDescription = "Pasión",
                            modifier = Modifier.height(80.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(text = "Pasión", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont)
                        Text("Amamos Stardew Valley tanto como tú y eso se refleja en cada producto.", textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.comunidad),
                            contentDescription = "Comunidad",
                            modifier = Modifier.fillMaxWidth(0.8f).height(180.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(text = "Comunidad", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont)
                        Text("Creemos en el poder de unir a los jugadores chilenos del valle.", textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.junimoss),
                            contentDescription = "Calidad",
                            modifier = Modifier.height(80.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(text = "Calidad", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont)
                        Text("Solo ofrecemos productos que cumplen con nuestros altos estándares.", textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para la sección "Únete a Nuestra Comunidad".
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.comunidad),
                            contentDescription = "Únete a Nuestra Comunidad",
                            modifier = Modifier.fillMaxWidth(0.8f).height(180.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Únete a Nuestra Comunidad",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "En Junimos Store no solo vendemos productos, creamos experiencias. Formamos parte de una comunidad vibrante de jugadores que comparten consejos, historias y su amor por Stardew Valley.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.developed_stardew), // Imagen de créditos.
                    contentDescription = "Developed by ConcernedApe",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}