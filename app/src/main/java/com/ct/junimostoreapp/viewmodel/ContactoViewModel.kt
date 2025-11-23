package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Patterns

class ContactoViewModel : ViewModel() { // ViewModel para la pantalla de contacto.
    var uiState by mutableStateOf(ContactoUiState()) // Estado de la UI, observable por la vista.
        private set

    // Funciones para actualizar los campos del formulario en el estado de la UI.
    fun onNameChange(name: String) {
        uiState = uiState.copy(name = name, error = null)
    }

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email, error = null)
    }

    fun onAsuntoChange(asunto: String) {
        uiState = uiState.copy(asunto = asunto, error = null)
    }

    fun onMessageChange(message: String) {
        uiState = uiState.copy(message = message, error = null)
    }

    fun submit() { // Función para enviar el formulario de contacto.
        // Validaciones de los campos del formulario.
        if (uiState.name.trim().length < 3) {
            uiState = uiState.copy(error = "El nombre debe tener al menos 3 caracteres.")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
            uiState = uiState.copy(error = "El correo no tiene un formato válido.")
            return
        }
        if (uiState.asunto.trim().length < 3) {
            uiState = uiState.copy(error = "El asunto debe tener al menos 3 caracteres.")
            return
        }
        if (uiState.message.length < 10) {
            uiState = uiState.copy(error = "El mensaje debe tener al menos 10 caracteres.")
            return
        }
        // Lógica para enviar el mensaje de contacto
        uiState = uiState.copy(successMessage = "¡Mensaje enviado con éxito!") // Actualiza el estado con un mensaje de éxito.
    }

    fun dismissMessages() { // Función para limpiar los mensajes de error y éxito.
        uiState = uiState.copy(error = null, successMessage = null)
        // Limpiar formulario
        uiState = uiState.copy(name = "", email = "", asunto = "", message = "") // Limpia los campos del formulario.
    }
}

data class ContactoUiState( // Define el estado de la UI para la pantalla de contacto.
    val name: String = "",
    val email: String = "",
    val asunto: String = "",
    val message: String = "",
    val error: String? = null,
    val successMessage: String? = null
)
