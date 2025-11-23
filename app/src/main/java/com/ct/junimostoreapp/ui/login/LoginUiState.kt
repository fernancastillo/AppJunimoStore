package com.ct.junimostoreapp.ui.login

data class LoginUiState ( // Define el estado de la UI para la pantalla de inicio de sesión.
    val username:String="", // Almacena el nombre de usuario (correo) ingresado.
    val password:String="", // Almacena la contraseña ingresada.
    val isLoading:Boolean = false, // Indica si se está realizando una operación de carga (ej. validando credenciales).
    val error:String? =null // Almacena un mensaje de error si ocurre alguno durante el inicio de sesión.
)
