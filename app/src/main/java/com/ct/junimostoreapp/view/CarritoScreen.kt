package com.ct.junimostoreapp.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.model.CartItem
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.CarritoViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, rut: String, carritoViewModel: CarritoViewModel) {
    var showClearCartDialog by remember { mutableStateOf(false) }
    var showRemoveItemDialog by remember { mutableStateOf<CartItem?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showProcessingDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val productos by carritoViewModel.productos.collectAsState(initial = emptyList())
    val darkGreen = Color(0xFF006400)
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower_regular))
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    LaunchedEffect(rut) {
        carritoViewModel.checkDuoc(rut)
    }

    if (showClearCartDialog) {
        AlertDialog(
            containerColor = AzulCielo,
            onDismissRequest = { showClearCartDialog = false },
            title = { Text("Vaciar Carrito") },
            text = { Text("¿Estás seguro de que quieres vaciar el carrito?") },
            confirmButton = {
                Button(
                    onClick = {
                        carritoViewModel.clearCart()
                        showClearCartDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Aceptar", color = Color.Black)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showClearCartDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    showRemoveItemDialog?.let { item ->
        AlertDialog(
            containerColor = AzulCielo,
            onDismissRequest = { showRemoveItemDialog = null },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que quieres eliminar este producto del carrito?") },
            confirmButton = {
                Button(
                    onClick = {
                        carritoViewModel.removeFromCart(item)
                        showRemoveItemDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Aceptar", color = Color.Black)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showRemoveItemDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showConfirmDialog) {
        val total = carritoViewModel.cartItems.sumOf { it.total }
        var totalConDescuento = if (carritoViewModel.descuentoAplicado) total - 1000 else total
        if (carritoViewModel.descuentoDuoc) {
            totalConDescuento = (totalConDescuento * 0.8).toInt()
        }

        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Compra") },
            text = { Text("¿Estás seguro de que quieres comprar los productos del carrito por un total de $${totalConDescuento}?") },
            containerColor = AzulCielo,
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        showProcessingDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Confirmar", color = Color.Black)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showProcessingDialog) {
        LaunchedEffect(Unit) {
            delay(2000)
            carritoViewModel.crearOrden(rut)
            showProcessingDialog = false
            showSuccessDialog = true
        }

        AlertDialog(
            onDismissRequest = {},
            containerColor = AzulCielo,
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = R.drawable.cargando,
                        contentDescription = "Cargando",
                        imageLoader = imageLoader,
                        modifier = Modifier.height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Procesando la compra",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = indieFlowerFont,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {}
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = AzulCielo,
            title = {
                Text(
                    "Compra realizada con éxito",
                    fontFamily = indieFlowerFont,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("pedidos/$rut") {
                                popUpTo("productos/$rut")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Carrito")
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.carrito),
                    contentDescription = "Carrito",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (carritoViewModel.cartItems.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AzulCielo
                        )
                    ) {
                        Text(
                            text = "El carrito está vacío",
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(carritoViewModel.cartItems) { item ->
                            val producto = productos.find { it.codigo == item.product.codigo }
                            if (producto != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = AmarilloMostaza
                                    )
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = item.product.imagen),
                                            contentDescription = item.product.nombre,
                                            modifier = Modifier.size(80.dp)
                                        )
                                        Spacer(modifier = Modifier.padding(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = item.product.nombre, fontWeight = FontWeight.Bold)
                                            Text(text = "Stock: ${producto.stock}")
                                            Text(text = "Total: $${item.total}")
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(
                                                    onClick = { carritoViewModel.decreaseQuantity(item) },
                                                    enabled = item.quantity > 1
                                                ) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                                                }
                                                Text(text = "${item.quantity}")
                                                IconButton(onClick = { carritoViewModel.increaseQuantity(item) }) {
                                                    Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                                }
                                            }
                                        }
                                        IconButton(onClick = { showRemoveItemDialog = item }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (carritoViewModel.descuentoDuoc) {
                        Text(
                            text = "20% de descuento extra por correo DUOC",
                            color = darkGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (carritoViewModel.descuentoAplicado) {
                        Card(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = CardDefaults.cardColors(containerColor = darkGreen)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("SV1000", fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.weight(1f))
                                VerticalDivider(color = AmarilloMostaza, thickness = 2.dp, modifier = Modifier.height(24.dp))
                                IconButton(onClick = { carritoViewModel.cancelarDescuento() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancelar descuento", tint = Color.White)
                                }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = carritoViewModel.descuentoCodigo,
                                onValueChange = carritoViewModel::onDescuentoChange,
                                label = { Text("Código de descuento") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = AmarilloMostaza,
                                    focusedContainerColor = AmarilloMostaza
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { carritoViewModel.aplicarDescuento() }, colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)) { 
                                Text("Aplicar", color = Color.Black)
                            }
                        }
                    }

                    carritoViewModel.descuentoMensaje?.let { 
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = if (carritoViewModel.descuentoAplicado) darkGreen else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    val total = carritoViewModel.cartItems.sumOf { it.total }
                    var totalConDescuento = if (carritoViewModel.descuentoAplicado) total - 1000 else total
                    if (carritoViewModel.descuentoDuoc) {
                        totalConDescuento = (totalConDescuento * 0.8).toInt()
                    }
                    Text("Total: $${totalConDescuento}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(
                            onClick = { showClearCartDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                        ) {
                            Text("Vaciar Carrito", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button( 
                            onClick = { showConfirmDialog = true }, 
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                        ) {
                            Text("Comprar", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}