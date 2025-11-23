package com.ct.junimostoreapp.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.repository.ProductoRepository
import com.ct.junimostoreapp.ui.theme.AmarilloMostaza
import com.ct.junimostoreapp.ui.theme.AzulCielo
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de componentes de Material 3 que aún están en fase experimental.
@Composable
fun QRScreen(navController: NavController, rut: String) { // Define la pantalla de escáner QR.
    val context = LocalContext.current // Obtiene el contexto actual.
    val application = context.applicationContext as ProductoApplication
    val repository = application.productoRepository
    val scope = rememberCoroutineScope()

    var hasCameraPermission by remember { // Estado para verificar si se tiene permiso de cámara.
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult( // Lanza la solicitud de permiso de cámara.
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> // Se ejecuta cuando el usuario responde a la solicitud de permiso.
            hasCameraPermission = granted
        }
    )
    var qrCodeInfo by remember { mutableStateOf<String?>(null) } // Almacena la información del código QR escaneado.
    var productExists by remember { mutableStateOf<Boolean?>(null) } // Indica si el producto escaneado existe.

    val options = GmsBarcodeScannerOptions.Builder() // Opciones para el escáner de códigos de barras.
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE) // Se especifica que solo se escanearán códigos QR.
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options) // Cliente para el escáner de códigos de barras.
    val indieFlowerFont = FontFamily(Font(R.font.indie_flower)) // Carga la fuente personalizada.

    Scaffold( // Estructura principal de la pantalla.
        topBar = {
            JunimoTopAppBar(navController = navController, rut = rut, title = "Lector QR") // Barra de aplicación superior.
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qr), // Imagen de cabecera de la sección.
                    contentDescription = "QR",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text( // Texto descriptivo.
                    text = "Escanea el código QR de un producto para ver sus detalles",
                    fontFamily = indieFlowerFont,
                    color = Color.White,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.8f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button( // Botón para activar la cámara y escanear el código QR.
                    onClick = {
                        if (hasCameraPermission) { // Si se tiene permiso de cámara, inicia el escaneo.
                            scanner.startScan()
                                .addOnSuccessListener { barcode -> // Se ejecuta si el escaneo es exitoso.
                                    qrCodeInfo = barcode.rawValue
                                    scope.launch {
                                        productExists = repository.getProductoPorCodigo(barcode.rawValue ?: "") != null // Verifica si el producto existe.
                                    }
                                }
                                .addOnFailureListener { // Se ejecuta si el escaneo falla.
                                    qrCodeInfo = "Error al escanear: ${it.message}"
                                    productExists = false
                                }
                        } else { // Si no se tiene permiso, lo solicita.
                            launcher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                ) {
                    Text("Activar cámara", color = Color.Black)
                }

                if (qrCodeInfo != null) { // Si se ha escaneado un código QR, muestra un diálogo.
                    AlertDialog(
                        onDismissRequest = { qrCodeInfo = null },
                        title = { Text("Información del Código QR") },
                        text = {
                            if (productExists == true) { // Si el producto existe, muestra un mensaje indicándolo.
                                Text("El producto con código '$qrCodeInfo' fue encontrado.")
                            } else { // Si el producto no existe, muestra el código escaneado.
                                Text("El código escaneado es: '$qrCodeInfo' y no corresponde a un producto.")
                            }
                        },
                        confirmButton = { // Botón de confirmación.
                            if (productExists == true) { // Si el producto existe, permite navegar a su detalle.
                                Button(
                                    onClick = {
                                        navController.navigate("producto_detalle/$rut/$qrCodeInfo")
                                        qrCodeInfo = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza)
                                ) {
                                    Text("Ver producto", color = Color.Black)
                                }
                            }
                        },
                        dismissButton = { // Botón para cerrar el diálogo.
                            Button(
                                onClick = { qrCodeInfo = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Cerrar")
                            }
                        },
                        containerColor = AzulCielo
                    )
                }
            }
        }
    }
}