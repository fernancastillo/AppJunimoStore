package com.ct.junimostoreapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() { // ViewModel que gestiona la lógica y el estado de la pantalla de inicio de sesión.

    var uiState by mutableStateOf(LoginUiState()) // Estado de la UI, observable por la vista.
        private set // El estado solo puede ser modificado dentro de este ViewModel.

    fun onUsernameChange(value: String) { // Función que se llama cuando el campo de nombre de usuario cambia.
        uiState = uiState.copy(username = value, error = null) // Actualiza el nombre de usuario en el estado y limpia cualquier error previo.
    }

    fun onpasswordChange(value: String) { // Función que se llama cuando el campo de contraseña cambia.
        uiState = uiState.copy(password = value, error = null) // Actualiza la contraseña en el estado y limpia cualquier error previo.
    }

    fun submit(onSuccess: (String, String) -> Unit) { // Función para enviar el formulario de inicio de sesión.
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null) // Pone el estado en modo de carga y limpia errores.
            val user = authRepository.login(uiState.username.trim(), uiState.password) // Llama al repositorio para validar las credenciales.

            uiState = uiState.copy(isLoading = false) // Desactiva el modo de carga.

            if (user != null) { // Si el inicio de sesión es exitoso.
                val route = if (user.tipo == "Admin") "dashboard/${user.nombre}" else "index/${user.run}" // Determina la ruta de navegación: "dashboard" para administradores, "index" para otros usuarios.
                onSuccess(route, user.run) // Llama a la función de éxito con la ruta y el RUN del usuario.
            } else { // Si el inicio de sesión falla.
                uiState = uiState.copy(error = "Credenciales Invalidas") // Actualiza el estado con un mensaje de error.
            }
        }
    }
}

class LoginViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}