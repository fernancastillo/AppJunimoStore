package com.ct.junimostoreapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun LugaresReunionScreen(navController: NavController, rut: String) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
        }
    )

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation = GeoPoint(location.latitude, location.longitude)
                    } else {
                        userLocation = GeoPoint(-33.4489, -70.6693) // Default to Santiago
                    }
                }
            } catch (e: SecurityException) {
                userLocation = GeoPoint(-33.4489, -70.6693) // Default on error
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) { // Usamos un Box para superponer elementos
        if (!hasLocationPermission) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Se necesita permiso de ubicación para mostrar el mapa.")
            }
        } else if (userLocation == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val santiago = GeoPoint(-33.4489, -70.6693)
            val valparaiso = GeoPoint(-33.0458, -71.6197)
            val concepcion = GeoPoint(-36.8269, -73.0498)
            val parqueOHiggins = GeoPoint(-33.465, -70.661)

            val mapView = rememberMapViewWithLifecycle(context)

            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            ) {
                it.setTileSource(TileSourceFactory.MAPNIK)
                it.controller.setZoom(15.0)
                it.controller.setCenter(userLocation)
                it.setMultiTouchControls(true) // <<< 1. HABILITAMOS EL ZOOM MULTITÁCTIL

                val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), it)
                myLocationOverlay.enableMyLocation()
                it.overlays.add(myLocationOverlay)

                addMarker(it, santiago, "Santiago", "Lugar de reunión en Santiago")
                addMarker(it, valparaiso, "Valparaíso", "Lugar de reunión en Valparaíso")
                addMarker(it, concepcion, "Concepción", "Lugar de reunión en Concepción")
                addMarker(it, parqueOHiggins, "Parque O'Higgins", "Punto de encuentro en el parque")
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd) // <<< 2. MOVEMOS EL BOTÓN A LA ESQUINA SUPERIOR DERECHA
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
        ) {
            Text("Volver", color = Color.Black)
        }
    }
}

private fun addMarker(mapView: MapView, geoPoint: GeoPoint, title: String, snippet: String) {
    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = title
    marker.snippet = snippet
    mapView.overlays.add(marker)
}

@Composable
private fun rememberMapViewWithLifecycle(context: Context): MapView {
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    DisposableEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        mapView.onResume()
        onDispose {
            mapView.onPause()
        }
    }
    return mapView
}
