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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun BlogScreen(navController: NavController, rut: String) { // Define la pantalla de blogs.
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular)) // Carga la fuente personalizada.
    var showGuiaDialog by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de la guía.
    var showSecretosDialog by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo de secretos.

    if (showGuiaDialog) { // Si `showGuiaDialog` es verdadero, se muestra el diálogo con la guía.
        AlertDialog(
            onDismissRequest = { showGuiaDialog = false }, // Cierra el diálogo al tocar fuera de él.
            title = { Text("Guía Completa de Stardew Valley", fontFamily = indieFlowerFont, fontSize = 28.sp, textAlign = TextAlign.Center) },
            text = { // Contenido del diálogo.
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // Columna que permite el desplazamiento vertical.
                    Text("¡Bienvenido al Valle!", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Stardew Valley es más que un simple juego de granja. Es un mundo lleno de secretos, personajes memorables y actividades infinitas. Si acabas de empezar, esta guía te ayudará a evitar errores comunes y maximizar tu diversión.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tus Primeros Días:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Día 1-7: Enfócate en limpiar un pequeño espacio en tu granja para cultivar. Los frijoles verdes y las papas son excelentes opciones iniciales.")
                    Text("Consejo: No gastes toda tu energía el primer día. Guarda algo para explorar el pueblo.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cultivos por Temporada:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Primavera: Fresas (después del Festival del Huevo)\nVerano: Arándanos (muy rentables)\nOtoño: Arándanos agrios")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Relaciones con los NPCs:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Cada personaje tiene gustos únicos. Por ejemplo:\nAbigail: Amatistas y cuarzo\nShane: Pizza y cerveza\nLeah: Ensalada y vino")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Herramientas Básicas", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Mejora tus herramientas en la herrería en este orden:\nHacha (para madera)\nPico (para minerales)\nRegadera (para cultivos más grandes)")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("¡Recuerda que lo más importante es disfrutar del proceso! No hay una forma \"correcta\" de jugar Stardew Valley.")
                }
            },
            confirmButton = { // Botón para cerrar el diálogo.
                Button(onClick = { showGuiaDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)) {
                    Text("Cerrar", color = Color.Black)
                }
            },
            containerColor = AzulCielo // Color de fondo del diálogo.
        )
    }

    if (showSecretosDialog) { // Si `showSecretosDialog` es verdadero, se muestra el diálogo con los secretos.
        AlertDialog(
            onDismissRequest = { showSecretosDialog = false },
            title = { Text("Los Secretos Mejor Guardados", fontFamily = indieFlowerFont, fontSize = 28.sp, textAlign = TextAlign.Center) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Secretos del Valle:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Stardew Valley está lleno de misterios y contenido oculto. Aquí te revelamos algunos de los mejores secretos:")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("El Mercado Negro de los Viernes:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Cada viernes, visita el camión junto a la casa de Marnie. Allí encontrarás a una misteriosa vendedora que ofrece objetos raros y prohibidos.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("El Fantasma del Cementerio:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Visita el cementerio en la noche del día 1 de cada temporada. Si tienes suerte, podrás ver una aparición fantasmagórica.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("La Cueva del Bosque Secreto:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("En el noroeste del Bosque Ceniciento, hay un tronco grande que puedes romper con un hacha de acero. Detrás encontrarás una cueva secreta con recursos únicos.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Referencias a Otros Juegos:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("En la biblioteca, busca el libro \"El Príncipe de los Guisantes\"\nEl nombre \"Junimo\" es un homenaje a los espíritus del bosque\nLas minas tienen referencias a juegos de rol clásicos")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("La Espada Galaxy:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Para obtener esta poderosa arma:\n\nLlega al nivel 120 de las minas\nConsigue una barra de iridio\nOfrécesela al altar en el desierto")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Arte Oculto:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Algunos cuadros en las casas de los NPCs cambian según eventos especiales. Presta atención a los detalles.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("La Gallina de Oro:", fontWeight = FontWeight.Bold, fontFamily = indieFlowerFont, fontSize = 20.sp)
                    Text("Existe una pequeña posibilidad de que una gallina ponga un huevo de oro. ¡Es extremadamente raro!")
                }
            },
            confirmButton = {
                Button(onClick = { showSecretosDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)) {
                    Text("Cerrar", color = Color.Black)
                }
            },
            containerColor = AzulCielo
        )
    }

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Blogs") // Barra de aplicación superior.
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
                contentScale = ContentScale.Crop, // Escala la imagen para que llene el contenedor.
                modifier = Modifier.fillMaxSize()
            )
            Column( // Columna que organiza las tarjetas de los blogs.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blog), // Imagen de cabecera de la sección de blogs.
                    contentDescription = "Blog",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para el primer artículo del blog.
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Guía Completa de Stardew Valley para Principiantes",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.sv),
                            contentDescription = "Stardew Valley",
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Descubre los secretos para comenzar tu aventura en el valle. Desde la creación de tu granja hasta las relaciones con los habitantes del pueblo.",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button( // Botón para leer el artículo completo.
                            onClick = { showGuiaDialog = true }, // Muestra el diálogo de la guía.
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                        ) {
                            Text("Leer Artículo Completo", color = Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card( // Tarjeta para el segundo artículo del blog.
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulCielo
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Los Secretos Mejor Guardados de Stardew Valley",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = indieFlowerFont
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.svg),
                            contentDescription = "Stardew Valley Guide",
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Exploramos los easter eggs, lugares secretos y contenido oculto que muchos jugadores se pierden en su primera partida.",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button( // Botón para leer el artículo completo.
                            onClick = { showSecretosDialog = true }, // Muestra el diálogo de los secretos.
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                        ) {
                            Text("Leer Artículo Completo", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}