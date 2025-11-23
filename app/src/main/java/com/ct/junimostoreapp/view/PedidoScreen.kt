package com.ct.junimostoreapp.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.database.OrdenDatabase
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.PedidoViewModel
import com.ct.junimostoreapp.viewmodel.PedidoViewModelFactory

fun providePedidoViewModelFactory(context: Context): PedidoViewModelFactory {
    val database = OrdenDatabase.getDatabase(context)
    val repository = OrdenRepository(database.ordenDao())
    return PedidoViewModelFactory(repository)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoScreen(navController: NavController, rut: String, vm: PedidoViewModel = viewModel(factory = providePedidoViewModelFactory(LocalContext.current))) {
    LaunchedEffect(rut) {
        vm.getPedidosPorRut(rut)
    }
    val pedidos by vm.pedidos.collectAsState()

    Scaffold(
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Mis Pedidos")
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
                    painter = painterResource(id = R.drawable.pedidos),
                    contentDescription = "Pedidos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (pedidos.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AzulCielo
                        )
                    ) {
                        Text(
                            text = "Aún no ha realizado ningún pedido",
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(pedidos) { pedido ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = AzulCielo
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("N° Orden: ${pedido.orden.numeroOrden}", fontWeight = FontWeight.Bold)
                                    Text("Fecha: ${pedido.orden.fecha}")
                                    Text("Estado: ${pedido.orden.estadoEnvio}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Productos:", fontWeight = FontWeight.Bold)
                                    pedido.productos.forEach { producto ->
                                        Text("- ${producto.nombre} (x${producto.cantidad})")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Total: $${pedido.orden.total}", fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
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
}