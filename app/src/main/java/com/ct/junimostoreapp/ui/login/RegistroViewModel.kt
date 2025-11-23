package com.ct.junimostoreapp.ui.login

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

class RegistroViewModel(
    private val authRepository: AuthRepository,
    private val regionRepository: RegionRepository = RegionRepository() // Repositorio para obtener las regiones y comunas.
) : ViewModel() { // ViewModel para la pantalla de registro.

    var uiState by mutableStateOf(RegistroUiState()) // Estado de la UI, observable por la vista.
        private set

    init { // Bloque de inicialización.
        uiState = uiState.copy(regiones = regionRepository.getRegiones()) // Carga la lista de regiones al iniciar el ViewModel.
    }

    // Funciones para actualizar los campos del formulario en el estado de la UI.
    fun onRunChange(value: String) {
        uiState = uiState.copy(run = value, error = null)
    }

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

    fun onRegionChange(value: String) { // Función que se llama al seleccionar una nueva región.
        val comunas = uiState.regiones.find { it.nombre == value }?.comunas ?: emptyList() // Busca las comunas de la región seleccionada.
        uiState = uiState.copy(region = value, comuna = "", comunas = comunas, error = null) // Actualiza el estado con la nueva región y sus comunas.
    }

    fun onComunaChange(value: String) {
        uiState = uiState.copy(comuna = value, error = null)
    }

    fun onDireccionChange(value: String) {
        uiState = uiState.copy(direccion = value, error = null)
    }

    @RequiresApi(Build.VERSION_CODES.O) // Requiere API 26+ por el uso de `java.time`.
    fun submit(onSuccess: () -> Unit) { // Función para enviar el formulario de registro.
        viewModelScope.launch {
            uiState = uiState.copy(error = null) // Limpia cualquier error previo.

            // Validaciones de los campos del formulario.
            if (authRepository.isRunRegistered(uiState.run)) {
                uiState = uiState.copy(error = "El RUN ya se encuentra registrado.")
                return@launch
            }
            if (authRepository.isEmailRegistered(uiState.correo)) {
                uiState = uiState.copy(error = "El correo ya se encuentra registrado.")
                return@launch
            }
            if (!isValidRun(uiState.run)) {
                uiState = uiState.copy(error = "El RUN no es válido.")
                return@launch
            }
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
            if (uiState.contrasenha.length !in 6..10) {
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
            if (uiState.direccion.trim().length < 3) {
                uiState = uiState.copy(error = "La dirección debe tener al menos 3 caracteres.")
                return@launch
            }

            val newUser = Usuario( // Crea un nuevo objeto Usuario con los datos del formulario.
                run = uiState.run,
                nombre = uiState.nombre,
                apellidos = uiState.apellidos,
                correo = uiState.correo,
                contrasenha = uiState.contrasenha,
                telefono = uiState.telefono,
                fechaNacimiento = uiState.fechaNacimiento,
                region = uiState.region,
                comuna = uiState.comuna,
                direccion = uiState.direccion
            )
            authRepository.registrar(newUser) // Llama al repositorio para registrar al nuevo usuario.
            onSuccess() // Llama a la función de éxito.
        }
    }

    private fun isValidRun(run: String): Boolean { // Función para validar el formato del RUN chileno.
        val cleanRun = run.replace(Regex("[.-]"), "")
        if (cleanRun.length !in 8..9) return false

        return try {
            val rut = cleanRun.substring(0, cleanRun.length - 1).toInt()
            val dv = cleanRun.last().uppercaseChar()
            var m = 0
            var s = 1
            var t = rut
            while (t != 0) {
                s = (s + t % 10 * (9 - m++ % 6)) % 11
                t /= 10
            }
            val verifier = if (s != 0) (s + 47).toChar() else 'K'
            dv == verifier
        } catch (e: NumberFormatException) {
            false
        }
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

data class RegistroUiState( // Define el estado de la UI para la pantalla de registro.
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
    val isLoading: Boolean = false
)

class RegistroViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistroViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}