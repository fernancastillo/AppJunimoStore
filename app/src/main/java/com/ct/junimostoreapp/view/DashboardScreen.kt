package com.ct.junimostoreapp.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.database.OrdenDatabase
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.ct.junimostoreapp.ui.theme.indieFlower
import com.ct.junimostoreapp.viewmodel.DashboardViewModel
import com.ct.junimostoreapp.viewmodel.DashboardViewModelFactory

fun provideDashboardViewModelFactory(context: Context): DashboardViewModelFactory {
    val application = context.applicationContext as ProductoApplication
    val ordenDatabase = OrdenDatabase.getDatabase(context)
    val ordenRepository = OrdenRepository(ordenDatabase.ordenDao())
    return DashboardViewModelFactory(application.productoRepository, application.authRepository, ordenRepository)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, adminName: String) {
    val vm: DashboardViewModel = viewModel(factory = provideDashboardViewModelFactory(LocalContext.current))
    val stats by vm.stats.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.loadStats()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            DashboardTopAppBar(navController = navController, title = "Dashboard")
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
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.junimoshop),
                    contentDescription = "Junimo Shop",
                    modifier = Modifier.height(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Bienvenido, $adminName!",
                    fontSize = 32.sp,
                    fontFamily = indieFlower,
                    style = TextStyle(
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.8f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statsMap = mapOf(
                        "Total Productos" to stats.totalProductos,
                        "Productos Críticos" to stats.productosCriticos,
                        "Productos Sin Stock" to stats.productosSinStock,
                        "Total Usuarios" to stats.totalUsuarios,
                        "Admins" to stats.admins,
                        "Clientes" to stats.clientes,
                        "Total Órdenes" to stats.totalOrdenes,
                        "Órdenes Pendientes" to stats.ordenesPendientes,
                        "Órdenes Enviadas" to stats.ordenesEnviadas,
                        "Órdenes Entregadas" to stats.ordenesEntregadas,
                        "Órdenes Canceladas" to stats.ordenesCanceladas
                    )
                    items(statsMap.toList()) { (title, value) ->
                        StatsCard(title = title, value = value.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(title: String, value: String) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AzulCielo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = value)
        }
    }
}