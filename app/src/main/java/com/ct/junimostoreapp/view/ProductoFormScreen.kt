package com.ct.junimostoreapp.view

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.model.Resena
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.CarritoViewModel
import com.ct.junimostoreapp.viewmodel.ProductoViewModel
import com.ct.junimostoreapp.viewmodel.ProductoViewModelFactory

fun provideProductoViewModelFactory(context: Context): ProductoViewModelFactory {
    val application = context.applicationContext as ProductoApplication
    return ProductoViewModelFactory(application.productoRepository, application.authRepository)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(navController: NavController, rut: String, codigo: String, carritoViewModel: CarritoViewModel) {
    val context = LocalContext.current
    val vm: ProductoViewModel = viewModel(factory = provideProductoViewModelFactory(context))

    LaunchedEffect(codigo, rut) {
        vm.getProducto(codigo, rut)
    }

    val producto by vm.producto.collectAsState()
    val resenas by vm.resenas.collectAsState()
    var cantidad by remember { mutableStateOf(1) }
    var showAddedToCartDialog by remember { mutableStateOf(false) }
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular))

    Scaffold(
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Detalle del Producto")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
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
                if (producto != null) {
                    producto?.let { p ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = AzulCielo
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                val imagePainter = rememberSafePainterResource(
                                    imageId = p.imagen,
                                    defaultImageId = R.drawable.junimo
                                )

                                Image(
                                    painter = imagePainter,
                                    contentDescription = p.nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(p.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text("Precio: $${p.precio}")
                                if (p.stock > 0) {
                                    Text("Stock: ${p.stock}")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Cantidad: ")
                                        IconButton(onClick = { if (cantidad > 1) cantidad-- }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Restar")
                                        }
                                        Text(cantidad.toString())
                                        IconButton(onClick = { if (cantidad < p.stock) cantidad++ }) {
                                            Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            carritoViewModel.addToCart(p, cantidad)
                                            vm.reducirStock(p.codigo, cantidad)
                                            showAddedToCartDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                                    ) {
                                        Text("Agregar al carrito", color = Color.Black)
                                    }
                                } else {
                                    Text("Producto sin stock", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Reseñas",
                            fontSize = 32.sp,
                            fontFamily = indieFlowerFont,
                            color = Color.White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.8f),
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            )
                        )

                        resenas.forEach { resena ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = AzulCielo)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(resena.titulo, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Row { 
                                            for (i in 1..5) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = "Estrella",
                                                    tint = if (i <= resena.calificacion) Color.Yellow else Color.Gray
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(resena.texto)
                                }
                            }
                        }
                    }
                } else {
                    // Mostrar estado de carga
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AzulCielo)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cargando producto...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Volver", color = Color.Black)
                }
            }
        }
    }

    if (showAddedToCartDialog) {
        AlertDialog(
            onDismissRequest = { showAddedToCartDialog = false },
            containerColor = AzulCielo,
            title = { Text("Producto Añadido") },
            text = { Text("El producto ha sido añadido al carrito.") },
            confirmButton = {
                Button(
                    onClick = {
                        showAddedToCartDialog = false
                        navController.navigate("productos/$rut") {
                            popUpTo("productos/$rut") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Aceptar", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun rememberSafePainterResource(
    imageId: Int,
    defaultImageId: Int = R.drawable.junimo
): Painter {
    val context = LocalContext.current
    val resourceId = remember(imageId) {
        try {
            context.resources.getResourceName(imageId)
            imageId
        } catch (e: Resources.NotFoundException) {
            defaultImageId
        }
    }
    return painterResource(id = resourceId)
}
