package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.Usuario
import com.ct.junimostoreapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class PerfilViewModel(private val authRepository: AuthRepository) : ViewModel() { // ViewModel para la pantalla de perfil de usuario.
    var uiState by mutableStateOf(PerfilUiState()) // Estado de la UI, observable por la vista.
        private set

    fun loadUserProfile(rut: String) { // Función para cargar los datos del perfil de un usuario.
        viewModelScope.launch {
            val user = authRepository.getUsuarioPorRut(rut) // Obtiene el usuario desde el repositorio.
            if (user != null) { // Si el usuario existe, actualiza el estado.
                uiState = uiState.copy(usuario = user)
            }
        }
    }
}

data class PerfilUiState( // Define el estado de la UI para la pantalla de perfil.
    val usuario: Usuario? = null // Almacena los datos del usuario.
)

class PerfilViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}