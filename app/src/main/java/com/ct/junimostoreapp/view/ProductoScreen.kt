package com.ct.junimostoreapp.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(navController: NavController, rut: String) {
    val vm: ProductoViewModel = viewModel(factory = provideProductoViewModelFactory(LocalContext.current))
    val productos by vm.productos.collectAsState()
    val categorias by vm.categorias.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Productos")
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
            Column {
                Image(
                    painter = painterResource(id = R.drawable.productos),
                    contentDescription = "Productos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 16.dp, bottom = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = vm.filtroNombre,
                        onValueChange = vm::onFiltroNombreChange,
                        label = { Text("Buscar por nombre", color = Color.Black) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = AmarilloMostaza,
                            focusedContainerColor = AmarilloMostaza,
                            unfocusedIndicatorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedLabelColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        TextField(
                            value = vm.filtroCategoria,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría", color = Color.Black) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = AmarilloMostaza,
                                focusedContainerColor = AmarilloMostaza,
                                unfocusedIndicatorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedLabelColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            // ELIMINAMOS EL MODIFICADOR DE FONDO DE AQUÍ
                        ) {
                            categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria) },
                                    onClick = {
                                        vm.onFiltroCategoriaChange(categoria)
                                        expanded = false
                                    },
                                    // APLICAMOS EL ESTILO DIRECTAMENTE AL ÍTEM
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.Black, // Color del texto
                                    ),
                                    // APLICAMOS EL FONDO A CADA ÍTEM INDIVIDUAL
                                    modifier = Modifier.background(AmarilloMostaza)
                                )
                                if (categoria != categorias.last()) {
                                    HorizontalDivider(color = Color.Black.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .height(320.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AzulCielo
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = producto.imagen),
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = producto.nombre,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text("Precio: $${producto.precio}")
                                    if (producto.stock > 0) {
                                        Text("Stock: ${producto.stock}")
                                    } else {
                                        Text("Producto sin stock", color = Color.Red, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { navController.navigate("producto_detalle/$rut/${producto.codigo}") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AmarilloMostaza,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text("Ver detalles")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}