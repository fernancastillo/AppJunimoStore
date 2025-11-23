package com.ct.junimostoreapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ct.junimostoreapp.ProductoApplication
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import com.ct.junimostoreapp.login.LoginScreen
import com.ct.junimostoreapp.login.RegistroScreen
import com.ct.junimostoreapp.ui.home.IndexScreen
import com.ct.junimostoreapp.view.AdminOrdenesScreen
import com.ct.junimostoreapp.view.AdminProductosScreen
import com.ct.junimostoreapp.view.AdminUsuariosScreen
import com.ct.junimostoreapp.view.BlogScreen
import com.ct.junimostoreapp.view.CarritoScreen
import com.ct.junimostoreapp.view.ContactoScreen
import com.ct.junimostoreapp.view.DashboardScreen
import com.ct.junimostoreapp.view.DrawerMenu
import com.ct.junimostoreapp.view.EditarPerfilScreen
import com.ct.junimostoreapp.view.NosotrosScreen
import com.ct.junimostoreapp.view.PedidoScreen
import com.ct.junimostoreapp.view.PerfilScreen
import com.ct.junimostoreapp.view.ProductoFormScreen
import com.ct.junimostoreapp.view.ProductoScreen
import com.ct.junimostoreapp.view.QRScreen
import com.ct.junimostoreapp.view.SplashScreen
import com.ct.junimostoreapp.viewmodel.CarritoViewModel

class CarritoViewModelFactory(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(productoRepository, authRepository, ordenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav(){ // Define la navegación principal de la aplicación.
    val navController = rememberNavController() // Crea y recuerda un controlador de navegación.
    val context = LocalContext.current
    val app = context.applicationContext as ProductoApplication
    val carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModelFactory(app.productoRepository, app.authRepository, OrdenRepository(app.ordenDao)))

    NavHost(navController = navController, startDestination = "splash"){ // Contenedor que gestiona la navegación entre las diferentes pantallas.
        composable("splash"){
            SplashScreen(navController = navController)
        }

        composable("login"){
            LoginScreen(navController = navController)
        }

        composable("registro"){
            RegistroScreen(navController = navController)
        }

        composable("index/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de inicio, que recibe el RUT del usuario como argumento.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            IndexScreen(navController = navController, rut = rut)
        }

        composable("productos/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de productos, que recibe el RUT del usuario.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            ProductoScreen(navController = navController, rut = rut)
        }

        composable("perfil/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de perfil de usuario.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            PerfilScreen(navController = navController, rut = rut)
        }

        composable("carrito/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de carrito, que recibe el RUT del usuario.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            CarritoScreen(navController = navController, rut = rut, carritoViewModel = carritoViewModel)
        }

        composable("contacto/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de contacto.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            ContactoScreen(navController = navController, rut = rut)
        }

        composable("nosotros/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla "Sobre Nosotros".
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            NosotrosScreen(navController = navController, rut = rut)
        }

        composable("blogs/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de blogs.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            BlogScreen(navController = navController, rut = rut)
        }

        composable("qr/{rut}", arguments = listOf(navArgument("rut") { type = NavType.StringType })) { // Define la pantalla de escáner QR.
            backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            QRScreen(navController = navController, rut = rut)
        }

        composable(
            route = "pedidos/{rut}", // Define la pantalla que muestra el historial de pedidos del usuario.
            arguments = listOf(
                navArgument("rut") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            PedidoScreen(navController = navController, rut = rut)
        }

        composable(
            route = "editar_perfil/{rut}", // Define la pantalla para editar el perfil del usuario.
            arguments = listOf(
                navArgument("rut") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            EditarPerfilScreen(navController = navController, rut = rut)
        }

        composable(
            route = "producto_detalle/{rut}/{codigo}", // Define la pantalla que muestra los detalles de un producto.
            arguments = listOf(
                navArgument("rut") { type = NavType.StringType },
                navArgument("codigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            val codigo = backStackEntry.arguments?.getString("codigo") ?: ""
            ProductoFormScreen(navController = navController, rut = rut, codigo = codigo, carritoViewModel = carritoViewModel)
        }

        composable(
            route = "dashboard/{adminName}", // Define la pantalla del dashboard para administradores.
            arguments = listOf(
                navArgument("adminName") { type = NavType.StringType } // Recibe el nombre del administrador como argumento.
            )
        ) { backStackEntry ->
            val adminName = backStackEntry.arguments?.getString("adminName") ?: ""
            DashboardScreen(navController = navController, adminName = adminName)
        }

        composable("admin_productos") { // Define la pantalla de administración de productos.
            AdminProductosScreen(navController = navController)
        }

        composable("admin_usuarios") { // Define la pantalla de administración de usuarios.
            AdminUsuariosScreen(navController = navController)
        }

        composable("admin_ordenes") { // Define la pantalla de administración de órdenes.
            AdminOrdenesScreen(navController = navController)
        }

        composable(
            route = "DrawerMenu/{username}", // Define el menú lateral (drawer), que recibe el nombre de usuario.
            arguments = listOf(
                navArgument("username"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            DrawerMenu(username = username, navController = navController)
        }
    }
}