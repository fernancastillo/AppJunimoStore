package com.ct.junimostoreapp.view

import android.app.Application
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.AdminProductosViewModel
import com.ct.junimostoreapp.viewmodel.AdminProductosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(navController: NavController) { // Pantalla para la gestión de productos.
    val application = LocalContext.current.applicationContext as ProductoApplication
    val vm: AdminProductosViewModel = viewModel(factory = AdminProductosViewModelFactory(application.productoRepository))
    val productos by vm.productos.collectAsState() // Obtiene la lista de productos desde el ViewModel y la observa como un estado.

    Scaffold(
        topBar = {
            DashboardTopAppBar(navController = navController, title = "Gestión de Productos") // Barra de aplicación superior.
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding) // Padding para el contenido principal del Scaffold.
                .fillMaxSize(), // El Box ocupa todo el espacio disponible.
            contentAlignment = Alignment.Center // Centra el contenido dentro del Box.
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew), // Imagen de fondo.
                contentDescription = null, // No es necesario para imágenes decorativas.
                contentScale = ContentScale.Crop, // Escala la imagen para que llene el espacio, recortando si es necesario.
                modifier = Modifier.fillMaxSize() // La imagen ocupa todo el espacio del Box.
            )
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) { // Columna principal que organiza el contenido.
                Image(
                    painter = painterResource(id = R.drawable.productos), // Imagen de cabecera de la sección.
                    contentDescription = "Productos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp), // Altura fija para la imagen.
                    contentScale = ContentScale.Fit // La imagen se ajusta al espacio sin ser recortada.
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espaciador vertical.
                Button(
                    onClick = { vm.onAddProductClick() }, // Al hacer clic, se abre el diálogo para agregar un producto.
                    modifier = Modifier.fillMaxWidth(), // El botón ocupa todo el ancho.
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)) // Color de fondo del botón.
                ) {
                    Text("Agregar Producto", color = Color.White) // Texto del botón.
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espaciador vertical.
                LazyColumn { // Lista perezosa que solo renderiza los elementos visibles.
                    items(productos) { producto -> // Itera sobre la lista de productos.
                        Card( // Tarjeta que muestra la información de un producto.
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp), // Espaciado vertical entre tarjetas.
                            colors = CardDefaults.cardColors(containerColor = AzulCielo) // Color de fondo de la tarjeta.
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) { // Contenido de la tarjeta.
                                Text(text = "Código: ${producto.codigo}", fontWeight = FontWeight.Bold) // Muestra el código del producto.
                                Text(text = "Nombre: ${producto.nombre}") // Muestra el nombre del producto.
                                Text(text = "Categoría: ${producto.categoria}") // Muestra la categoría del producto.
                                Text(text = "Precio: ${producto.precio}") // Muestra el precio del producto.
                                Text(text = "Stock: ${producto.stock}") // Muestra el stock del producto.
                                Spacer(modifier = Modifier.padding(8.dp)) // Espaciador.
                                Row( // Fila para los botones de acción.
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End // Alinea los botones al final (derecha).
                                ) {
                                    Button(
                                        onClick = { vm.onEditProductClick(producto) }, // Al hacer clic, se abre el diálogo para editar el producto.
                                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza) // Color del botón.
                                    ) {
                                        Text("Editar", color = Color.Black) // Texto del botón.
                                    }
                                    Spacer(modifier = Modifier.width(8.dp)) // Espaciador horizontal.
                                    Button(
                                        onClick = { vm.onEliminarClick(producto) }, // Al hacer clic, se inicia el proceso de eliminación del producto.
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Color del botón.
                                    ) {
                                        Text("Eliminar", color = Color.White) // Texto del botón.
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (vm.showAddProductDialog) { // Si la variable de estado es verdadera, se muestra el diálogo para agregar producto.
            AddProductDialog(vm = vm)
        }

        if (vm.showEditProductDialog) { // Si la variable de estado es verdadera, se muestra el diálogo para editar producto.
            vm.editProductoState?.let { // Solo se muestra si hay un producto seleccionado para editar.
                EditProductDialog(vm = vm)
            }
        }

        if (vm.showAddSuccessDialog) { // Si la variable de estado es verdadera, se muestra un diálogo de éxito.
            AlertDialog(
                onDismissRequest = { vm.onAddSuccessDialogDismiss() }, // Acción al cerrar el diálogo.
                title = {
                    Text(
                        text = "Producto agregado con éxito",
                        textAlign = TextAlign.Center, // Centra el texto del título.
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = { // Botón de confirmación.
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { // Centra el botón horizontalmente.
                        Button(
                            onClick = { vm.onAddSuccessDialogDismiss() }, // Cierra el diálogo al hacer clic.
                            colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                        ) {
                            Text("Aceptar", color = Color.Black)
                        }
                    }
                },
                containerColor = AmarilloMostaza // Color de fondo del diálogo.
            )
        }

        if (vm.showEditSuccessDialog) { // Si la variable de estado es verdadera, se muestra un diálogo de éxito de edición.
            AlertDialog(
                onDismissRequest = { vm.onEditSuccessDialogDismiss() }, // Acción al cerrar el diálogo.
                title = {
                    Text(
                        text = "Producto editado con éxito",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = { // Botón de confirmación.
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = { vm.onEditSuccessDialogDismiss() }, // Cierra el diálogo al hacer clic.
                            colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                        ) {
                            Text("Aceptar", color = Color.Black)
                        }
                    }
                },
                containerColor = AmarilloMostaza // Color de fondo del diálogo.
            )
        }

        if (vm.showConfirmDialog) { // Si la variable de estado es verdadera, se muestra el diálogo de confirmación de eliminación.
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() }, // Acción al cerrar el diálogo.
                title = { Text("Confirmar Eliminación") },
                text = { // Contenido del diálogo.
                    Column {
                        Text("¿Seguro que desea eliminar este producto?")
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(text = "Código: ${vm.selectedProducto?.codigo}", fontWeight = FontWeight.Bold)
                        Text(text = "Nombre: ${vm.selectedProducto?.nombre}")
                    }
                },
                confirmButton = { // Botón de confirmación.
                    Button(
                        onClick = { vm.onConfirmEliminar() }, // Ejecuta la eliminación.
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                dismissButton = { // Botón para cancelar.
                    Button(
                        onClick = { vm.onDialogDismiss() }, // Cierra el diálogo.
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancelar")
                    }
                },
                containerColor = AzulCielo // Color de fondo del diálogo.
            )
        }

        if (vm.showSuccessDialog) { // Si la variable de estado es verdadera, se muestra el diálogo de éxito de eliminación.
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() }, // Acción al cerrar el diálogo.
                title = { Text("Producto Eliminado") },
                text = { Text("El producto ha sido eliminado con éxito.") },
                confirmButton = { // Botón de confirmación.
                    Button(
                        onClick = { vm.onDialogDismiss() }, // Cierra el diálogo.
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                containerColor = AzulCielo // Color de fondo del diálogo.
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(vm: AdminProductosViewModel) { // Composable para el diálogo de agregar un nuevo producto.
    val newProducto = vm.newProductoState // Obtiene el estado del nuevo producto desde el ViewModel.
    var expanded by remember { mutableStateOf(false) } // Estado para controlar si el menú desplegable está expandido.
    val textFieldColors = OutlinedTextFieldDefaults.colors( // Define los colores de los campos de texto.
        unfocusedContainerColor = AzulCielo,
        focusedContainerColor = AzulCielo
    )

    AlertDialog(
        onDismissRequest = { vm.onAddProductDismiss() }, // Cierra el diálogo al presionar fuera de él.
        title = { Text("Agregar Nuevo Producto", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }, // Título del diálogo.
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // Columna que permite el desplazamiento vertical.
                OutlinedTextField(value = newProducto.codigo, onValueChange = vm::onNewCodigoChange, label = { Text("Código") }, colors = textFieldColors)
                OutlinedTextField(value = newProducto.nombre, onValueChange = vm::onNewNombreChange, label = { Text("Nombre") }, colors = textFieldColors)

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) { // Contenedor para el menú desplegable (combobox).
                    OutlinedTextField(
                        value = newProducto.categoria, // Valor actual de la categoría.
                        onValueChange = {}, // No se hace nada al cambiar, es de solo lectura.
                        label = { Text("Categoría") },
                        readOnly = true, // El campo es de solo lectura.
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, // Ícono del menú desplegable.
                        modifier = Modifier.menuAnchor(), // Ancla el menú al campo de texto.
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { // Menú que se despliega.
                        vm.categorias.forEach { categoria -> // Itera sobre las categorías del ViewModel.
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = { // Acción al hacer clic en una categoría.
                                    vm.onNewCategoriaChange(categoria) // Actualiza la categoría en el ViewModel.
                                    expanded = false // Cierra el menú.
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = newProducto.descripcion, onValueChange = vm::onNewDescripcionChange, label = { Text("Descripción") }, colors = textFieldColors)
                OutlinedTextField(value = newProducto.precio.toString(), onValueChange = vm::onNewPrecioChange, label = { Text("Precio") }, colors = textFieldColors)
                OutlinedTextField(value = newProducto.stock.toString(), onValueChange = vm::onNewStockChange, label = { Text("Stock") }, colors = textFieldColors)
                OutlinedTextField(value = newProducto.stockCritico.toString(), onValueChange = vm::onNewStockCriticoChange, label = { Text("Stock Crítico") }, colors = textFieldColors)
                vm.error?.let { // Si hay un error, lo muestra.
                    Text(it, color = MaterialTheme.colorScheme.error) // Muestra el mensaje de error.
                }
            }
        },
        confirmButton = { // Botón para confirmar y agregar el producto.
            Button(onClick = { vm.submitNewProducto() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Agregar") }
        },
        dismissButton = { // Botón para cancelar.
            Button(onClick = { vm.onAddProductDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Cancelar") }
        },
        containerColor = AmarilloMostaza // Color de fondo del diálogo.
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(vm: AdminProductosViewModel) { // Composable para el diálogo de editar un producto.
    val editProducto = vm.editProductoState // Obtiene el estado del producto a editar.
    var expanded by remember { mutableStateOf(false) } // Estado para el menú desplegable.
    val textFieldColors = OutlinedTextFieldDefaults.colors( // Colores para los campos de texto.
        unfocusedContainerColor = AzulCielo,
        focusedContainerColor = AzulCielo
    )

    AlertDialog(
        onDismissRequest = { vm.onEditProductDismiss() }, // Cierra el diálogo al presionar fuera.
        title = { Text("Editar Producto", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }, // Título del diálogo.
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) { // Columna con desplazamiento vertical.
                OutlinedTextField(value = editProducto!!.codigo, onValueChange = {}, label = { Text("Código") }, colors = textFieldColors, enabled = false) // Campo no editable para el código.
                OutlinedTextField(value = editProducto.nombre, onValueChange = vm::onEditNombreChange, label = { Text("Nombre") }, colors = textFieldColors)

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) { // Contenedor para el menú desplegable.
                    OutlinedTextField(
                        value = editProducto.categoria,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true, // Campo de solo lectura.
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, // Ícono del menú.
                        modifier = Modifier.menuAnchor(), // Ancla el menú al campo.
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { // Menú desplegable.
                        vm.categorias.forEach { categoria -> // Itera sobre las categorías.
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = { // Acción al hacer clic.
                                    vm.onEditCategoriaChange(categoria) // Actualiza la categoría en el ViewModel.
                                    expanded = false // Cierra el menú.
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = editProducto.descripcion, onValueChange = vm::onEditDescripcionChange, label = { Text("Descripción") }, colors = textFieldColors)
                OutlinedTextField(value = editProducto.precio.toString(), onValueChange = vm::onEditPrecioChange, label = { Text("Precio") }, colors = textFieldColors)
                OutlinedTextField(value = editProducto.stock.toString(), onValueChange = vm::onEditStockChange, label = { Text("Stock") }, colors = textFieldColors)
                OutlinedTextField(value = editProducto.stockCritico.toString(), onValueChange = vm::onEditStockCriticoChange, label = { Text("Stock Crítico") }, colors = textFieldColors)
                vm.error?.let { // Si hay un error, lo muestra.
                    Text(it, color = MaterialTheme.colorScheme.error) // Muestra el mensaje de error.
                }
            }
        },
        confirmButton = { // Botón para guardar los cambios.
            Button(onClick = { vm.submitEditProducto() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Guardar") }
        },
        dismissButton = { // Botón para cancelar la edición.
            Button(onClick = { vm.onEditProductDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Cancelar") }
        },
        containerColor = AmarilloMostaza // Color de fondo del diálogo.
    )
}
