package com.ct.junimostoreapp.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ct.junimostoreapp.ui.login.LoginUiState
import com.ct.junimostoreapp.ui.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de UI para LoginScreen.
 *
 * PROPÓSITO PRINCIPAL:
 * Validar que la UI reacciona correctamente a los cambios de estado del ViewModel,
 * específicamente cuando se produce un error de inicio de sesión.
 */
class LoginScreenTest {

    // Regla para crear un entorno de prueba de Compose para una actividad
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun cuando_el_login_falla_el_mensaje_de_error_debe_ser_visible() {
        // 1. Preparación (Arrange)
        val mockViewModel: LoginViewModel = mockk(relaxed = true)
        val mensajeError = "Usuario o contraseña incorrectos"

        // Configuramos el estado que queremos que el ViewModel devuelva
        val estadoConError = LoginUiState(error = mensajeError)

        // Usamos `every` para simular que, cuando se acceda a `uiState`, se devuelva nuestro estado con error.
        every { mockViewModel.uiState } returns estadoConError

        // 2. Acción (Act)
        // Renderizamos la pantalla de Login con nuestro ViewModel mockeado
        composeRule.setContent {
            LoginScreen(navController = mockk(relaxed = true), vm = mockViewModel)
        }

        // 3. Verificación (Assert)
        // Buscamos un nodo de texto que contenga el mensaje de error y
        // verificamos que existe y es visible en la pantalla.
        composeRule.onNodeWithText(mensajeError).assertExists().assertIsDisplayed()
    }
}
