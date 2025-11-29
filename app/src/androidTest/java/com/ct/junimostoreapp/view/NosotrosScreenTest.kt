package com.ct.junimostoreapp.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de UI para NosotrosScreen.
 *
 * PROPÓSITO PRINCIPAL:
 * Validar que los elementos estáticos y clave de la pantalla "Nosotros"
 * se renderizan y son visibles para el usuario.
 */
class NosotrosScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun la_pantalla_nosotros_debe_mostrar_el_titulo_y_el_boton_de_reunion() {
        // 1. Preparación y Acción (Arrange & Act)
        // Renderizamos la pantalla "Nosotros"
        composeRule.setContent {
            NosotrosScreen(navController = mockk(relaxed = true), rut = "")
        }

        // 2. Verificación (Assert)

        // Verificamos que el título "Nuestra Historia" existe y es visible
        composeRule.onNodeWithText("Nuestra Historia").assertExists().assertIsDisplayed()

        // Hacemos scroll hasta el botón y LUEGO verificamos si es visible
        composeRule.onNodeWithText("Lugares de reunión")
            .performScrollTo() // <<< ¡AQUÍ ESTÁ LA SOLUCIÓN!
            .assertIsDisplayed()
    }

    @Test
    fun la_seccion_de_mision_debe_ser_visible() {
        // 1. Preparación y Acción (Arrange & Act)
        composeRule.setContent {
            NosotrosScreen(navController = mockk(relaxed = true), rut = "")
        }

        // 2. Verificación (Assert)
        // Verificamos que el título "Misión" existe y es visible
        composeRule.onNodeWithText("Misión").assertExists().assertIsDisplayed()
    }
}
