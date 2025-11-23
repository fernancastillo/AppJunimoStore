package com.ct.junimostoreapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.Region
import com.ct.junimostoreapp.data.model.Usuario
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.RegionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class EditarPerfilViewModel(
    private val authRepository: AuthRepository,
    private val regionRepository: RegionRepository = RegionRepository() // Repositorio para obtener las regiones y comunas.
) : ViewModel() { // ViewModel para la pantalla de edición de perfil.

    var uiState by mutableStateOf(EditarPerfilUiState()) // Estado de la UI, observable por la vista.
        private set

    @RequiresApi(Build.VERSION_CODES.O) // Requiere API 26+ por el uso de `java.time`.
    fun loadUserProfile(rut: String) { // Carga los datos del perfil del usuario.
        viewModelScope.launch {
            val user = authRepository.getUsuarioPorRut(rut) // Obtiene el usuario desde el repositorio.
            if (user != null) {
                val formattedDate = user.fechaNacimiento?.let { // Formatea la fecha de nacimiento.
                    try {
                        val parsedDate = LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        parsedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    } catch (e: Exception) {
                        it // Si falla el formato, muestra la fecha original.
                    }
                } ?: ""

                uiState = uiState.copy( // Actualiza el estado de la UI con los datos del usuario.
                    run = user.run,
                    nombre = user.nombre,
                    apellidos = user.apellidos,
                    correo = user.correo,
                    telefono = user.telefono ?: "",
                    fechaNacimiento = formattedDate,
                    region = user.region ?: "",
                    comuna = user.comuna ?: "",
                    direccion = user.direccion ?: "",
                    regiones = regionRepository.getRegiones(),
                    comunas = regionRepository.getRegiones().find { it.nombre == user.region }?.comunas ?: emptyList()
                )
            }
        }
    }

    // Funciones para actualizar los campos del formulario en el estado de la UI.
    fun onNombreChange(value: String) {
        uiState = uiState.copy(nombre = value, error = null)
    }

    fun onApellidosChange(value: String) {
        uiState = uiState.copy(apellidos = value, error = null)
    }

    fun onCorreoChange(value: String) {
        uiState = uiState.copy(correo = value, error = null)
    }

    fun onContrasenhaChange(value: String) {
        uiState = uiState.copy(contrasenha = value, error = null)
    }

    fun onConfirmContrasenhaChange(value: String) {
        uiState = uiState.copy(confirmContrasenha = value, error = null)
    }

    fun onTelefonoChange(value: String) {
        uiState = uiState.copy(telefono = value, error = null)
    }

    fun onFechaNacimientoChange(value: String) {
        uiState = uiState.copy(fechaNacimiento = value, error = null)
    }

    fun onRegionChange(value: String) {
        val comunas = uiState.regiones.find { it.nombre == value }?.comunas ?: emptyList()
        uiState = uiState.copy(region = value, comuna = "", comunas = comunas, error = null)
    }

    fun onComunaChange(value: String) {
        uiState = uiState.copy(comuna = value, error = null)
    }

    fun onDireccionChange(value: String) {
        uiState = uiState.copy(direccion = value, error = null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submit(onSuccess: () -> Unit) { // Función para enviar el formulario de edición.
        viewModelScope.launch {
            uiState = uiState.copy(error = null) // Limpia cualquier error previo.

            // Validaciones de los campos del formulario.
            if (uiState.nombre.trim().length < 3) {
                uiState = uiState.copy(error = "El nombre debe tener al menos 3 caracteres.")
                return@launch
            }
            if (uiState.apellidos.trim().length < 3) {
                uiState = uiState.copy(error = "Los apellidos deben tener al menos 3 caracteres.")
                return@launch
            }
            if (!isValidEmail(uiState.correo)) {
                uiState = uiState.copy(error = "El correo debe ser @duoc.cl, @profesor.duoc.cl o @gmail.com.")
                return@launch
            }
            if (uiState.contrasenha.isNotEmpty() && uiState.contrasenha.length !in 6..10) {
                uiState = uiState.copy(error = "La contraseña debe tener entre 6 y 10 caracteres.")
                return@launch
            }
            if (uiState.contrasenha != uiState.confirmContrasenha) {
                uiState = uiState.copy(error = "Las contraseñas no coinciden.")
                return@launch
            }
            if (uiState.telefono.isNotEmpty() && !isValidPhone(uiState.telefono)) {
                uiState = uiState.copy(error = "El teléfono debe tener 9 dígitos y comenzar con 9.")
                return@launch
            }
            if (!isOfLegalAge(uiState.fechaNacimiento)) {
                uiState = uiState.copy(error = "Debes tener al menos 10 años para registrarte.")
                return@launch
            }
            if (uiState.region.isEmpty() || uiState.region == "Seleccione región") {
                uiState = uiState.copy(error = "Debes seleccionar una región.")
                return@launch
            }
            if (uiState.comuna.isEmpty() || uiState.comuna == "Seleccione comuna") {
                uiState = uiState.copy(error = "Debes seleccionar una comuna.")
                return@launch
            }
            if (uiState.direccion.trim().length < 3) {
                uiState = uiState.copy(error = "La dirección debe tener al menos 3 caracteres.")
                return@launch
            }

            val updatedUser = Usuario( // Crea un objeto Usuario con los datos actualizados.
                run = uiState.run,
                nombre = uiState.nombre,
                apellidos = uiState.apellidos,
                correo = uiState.correo,
                contrasenha = if (uiState.contrasenha.isNotEmpty()) uiState.contrasenha else authRepository.getUsuarioPorRut(uiState.run)!!.contrasenha, // Si la contraseña no se modificó, se mantiene la anterior.
                telefono = uiState.telefono,
                fechaNacimiento = uiState.fechaNacimiento,
                region = uiState.region,
                comuna = uiState.comuna,
                direccion = uiState.direccion
            )
            authRepository.actualizarUsuario(updatedUser) // Llama al repositorio para actualizar el usuario.
            uiState = uiState.copy(successMessage = "¡Perfil editado con éxito!") // Actualiza el estado con un mensaje de éxito.
            onSuccess()
        }
    }

    fun dismissSuccessMessage() { // Función para limpiar el mensaje de éxito.
        uiState = uiState.copy(successMessage = null)
    }

    private fun isValidEmail(email: String): Boolean { // Función para validar el dominio del correo electrónico.
        val validDomains = listOf("@duoc.cl", "@profesor.duoc.cl", "@gmail.com")
        return validDomains.any { email.endsWith(it) }
    }

    private fun isValidPhone(phone: String): Boolean { // Función para validar el formato del número de teléfono.
        if (phone.length != 9) return false
        if (phone.first() != '9') return false
        return phone.all { it.isDigit() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isOfLegalAge(birthDate: String): Boolean { // Función para validar que el usuario sea mayor de 10 años.
        if (birthDate.isBlank()) return false
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(birthDate, formatter)
            val age = Period.between(date, LocalDate.now()).years
            age >= 10
        } catch (e: Exception) {
            false
        }
    }
}

data class EditarPerfilUiState( // Define el estado de la UI para la pantalla de edición de perfil.
    val run: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val contrasenha: String = "",
    val confirmContrasenha: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val region: String = "",
    val comuna: String = "",
    val direccion: String = "",
    val regiones: List<Region> = emptyList(),
    val comunas: List<String> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null
)

class EditarPerfilViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditarPerfilViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}