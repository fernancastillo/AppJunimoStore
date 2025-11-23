package com.ct.junimostoreapp.view

import android.content.Context
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
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.database.OrdenDatabase
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.viewmodel.AdminOrdenesViewModel
import com.ct.junimostoreapp.viewmodel.AdminOrdenesViewModelFactory

fun provideAdminOrdenesViewModel(context: Context): AdminOrdenesViewModelFactory {
    val database = OrdenDatabase.getDatabase(context)
    val repository = OrdenRepository(database.ordenDao())
    return AdminOrdenesViewModelFactory(repository)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdenesScreen(navController: NavController, vm: AdminOrdenesViewModel = viewModel(factory = provideAdminOrdenesViewModel(LocalContext.current))) {
    val ordenes by vm.ordenes.collectAsState()

    Scaffold(
        topBar = {
            DashboardTopAppBar(navController = navController, title = "Gestión de Órdenes")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondostardew),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ordenes),
                    contentDescription = "Órdenes",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(ordenes) { ordenConProductos ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = AzulCielo)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "N° Orden: ${ordenConProductos.orden.numeroOrden}", fontWeight = FontWeight.Bold)
                                Text(text = "Fecha: ${ordenConProductos.orden.fecha}")
                                Text(text = "RUN Cliente: ${ordenConProductos.orden.run}")
                                Text(text = "Estado: ${ordenConProductos.orden.estadoEnvio}")
                                Text(text = "Total: $${ordenConProductos.orden.total}")
                                Spacer(modifier = Modifier.padding(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { vm.onEditOrdenClick(ordenConProductos.orden) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                                    ) {
                                        Text("Editar", color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { vm.onEliminarClick(ordenConProductos.orden) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Text("Eliminar", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (vm.showEditOrdenDialog) {
            vm.editOrdenState?.let {
                EditOrdenDialog(vm = vm)
            }
        }

        if (vm.showEditSuccessDialog) {
            AlertDialog(
                onDismissRequest = { vm.onEditSuccessDialogDismiss() },
                title = {
                    Text(
                        text = "Orden editada con éxito",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = { vm.onEditSuccessDialogDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)
                        ) {
                            Text("Aceptar", color = Color.Black)
                        }
                    }
                },
                containerColor = AmarilloMostaza
            )
        }

        if (vm.showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() },
                title = { Text("Confirmar Eliminación") },
                text = {
                    Column {
                        Text("¿Seguro que desea eliminar esta orden?")
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(text = "N° Orden: ${vm.selectedOrden?.numeroOrden}", fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { vm.onConfirmEliminar() },
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { vm.onDialogDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancelar")
                    }
                },
                containerColor = AzulCielo
            )
        }

        if (vm.showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { vm.onDialogDismiss() },
                title = { Text("Orden Eliminada") },
                text = { Text("La orden ha sido eliminada con éxito.") },
                confirmButton = {
                    Button(
                        onClick = { vm.onDialogDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                    ) {
                        Text("Aceptar", color = Color.Black)
                    }
                },
                containerColor = AzulCielo
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrdenDialog(vm: AdminOrdenesViewModel) {
    val editOrden = vm.editOrdenState
    var expanded by remember { mutableStateOf(false) }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = AzulCielo,
        focusedContainerColor = AzulCielo,
        disabledContainerColor = AzulCielo
    )

    AlertDialog(
        onDismissRequest = { vm.onEditOrdenDismiss() },
        title = { Text("Editar Orden", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = editOrden!!.numeroOrden, onValueChange = {}, label = { Text("N° Orden") }, colors = textFieldColors, enabled = false)
                OutlinedTextField(value = editOrden.fecha, onValueChange = {}, label = { Text("Fecha") }, colors = textFieldColors, enabled = false)
                OutlinedTextField(value = editOrden.run, onValueChange = {}, label = { Text("RUN Cliente") }, colors = textFieldColors, enabled = false)

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = editOrden.estadoEnvio,
                        onValueChange = {},
                        label = { Text("Estado") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        vm.estadosEnvio.forEach { estado ->
                            DropdownMenuItem(
                                text = { Text(estado) },
                                onClick = {
                                    vm.onEditEstadoEnvioChange(estado)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(value = editOrden.total.toString(), onValueChange = {}, label = { Text("Total") }, colors = textFieldColors, enabled = false)

                vm.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = { vm.submitEditOrden() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Guardar") }
        },
        dismissButton = {
            Button(onClick = { vm.onEditOrdenDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = AzulCielo)) { Text("Cancelar") }
        },
        containerColor = AmarilloMostaza
    )
}