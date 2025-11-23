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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class AdminUsuariosViewModel(private val authRepository: AuthRepository, private val regionRepository: RegionRepository = RegionRepository()) : ViewModel() { // ViewModel para la pantalla de administración de usuarios.

    // Flujo de estado mutable para la lista de usuarios. Es privado para controlar su modificación.
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    // Flujo de estado inmutable expuesto a la UI para observar la lista de usuarios.
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    // Estado para controlar la visibilidad del diálogo de confirmación de eliminación.
    var showConfirmDialog by mutableStateOf(false)
        private set // Solo el ViewModel puede modificar este estado.

    // Estado para controlar la visibilidad del diálogo de éxito tras una eliminación.
    var showSuccessDialog by mutableStateOf(false)
        private set

    // Almacena el usuario seleccionado para una acción.
    var selectedUsuario by mutableStateOf<Usuario?>(null)
        private set

    // Estado para controlar la visibilidad del diálogo de agregar usuario.
    var showAddUserDialog by mutableStateOf(false)
        private set

    // Estado para controlar la visibilidad del diálogo de editar usuario.
    var showEditUserDialog by mutableStateOf(false)
        private set

    // Estado para el diálogo de éxito al agregar un usuario.
    var showAddUserSuccessDialog by mutableStateOf(false)
        private set

    // Estado para el diálogo de éxito al editar un usuario.
    var showEditUserSuccessDialog by mutableStateOf(false)
        private set

    // Estado que representa la UI del formulario de registro.
    var registroUiState by mutableStateOf(RegistroUiState())
        private set

    // Estado que representa la UI del formulario de edición.
    var editUiState by mutableStateOf(RegistroUiState())
        private set

    init { // Bloque que se ejecuta al inicializar el ViewModel.
        loadUsuarios() // Carga la lista de usuarios.
        registroUiState = registroUiState.copy(regiones = regionRepository.getRegiones()) // Carga las regiones para el formulario.
    }

    private fun loadUsuarios() { // Función privada para cargar los usuarios.
        viewModelScope.launch { // Inicia una corutina.
            authRepository.getUsuarios().catch { e ->
                // Manejar error
            }.collect {
                _usuarios.value = it
            }
        }
    }

    fun onEliminarClick(usuario: Usuario) { // Se llama al hacer clic en el botón de eliminar.
        selectedUsuario = usuario // Guarda el usuario seleccionado.
        showConfirmDialog = true // Muestra el diálogo de confirmación.
    }

    fun onConfirmEliminar() { // Se llama al confirmar la eliminación.
        viewModelScope.launch {
            selectedUsuario?.let { // Si hay un usuario seleccionado...
                authRepository.removeUsuario(it) // ...lo elimina del repositorio.
            }
            showConfirmDialog = false // Oculta el diálogo de confirmación.
            showSuccessDialog = true // Muestra el diálogo de éxito.
        }
    }

    fun onDialogDismiss() { // Cierra los diálogos de eliminación.
        showConfirmDialog = false
        showSuccessDialog = false
        selectedUsuario = null
    }

    fun onAddUserClick() { // Se llama al hacer clic en el botón de agregar usuario.
        showAddUserDialog = true // Muestra el diálogo correspondiente.
    }

    fun onAddUserDismiss() { // Cierra el diálogo de agregar usuario.
        showAddUserDialog = false
        registroUiState = RegistroUiState(regiones = regionRepository.getRegiones()) // Reinicia el estado del formulario.
    }

    fun onAddUserSuccessDialogDismiss() { // Cierra el diálogo de éxito de agregación.
        showAddUserSuccessDialog = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEditUserClick(usuario: Usuario) { // Se llama al hacer clic en el botón de editar.
        val formattedDate = try { // Formatea la fecha de nacimiento para el selector.
            LocalDate.parse(usuario.fechaNacimiento, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        } catch (e: Exception) {
            usuario.fechaNacimiento // Si falla, usa el valor original.
        }

        editUiState = RegistroUiState( // Rellena el estado de edición con los datos del usuario.
            run = usuario.run,
            nombre = usuario.nombre,
            apellidos = usuario.apellidos,
            correo = usuario.correo,
            telefono = usuario.telefono,
            fechaNacimiento = formattedDate,
            tipo = usuario.tipo,
            region = usuario.region,
            comuna = usuario.comuna,
            direccion = usuario.direccion,
            regiones = regionRepository.getRegiones(),
            comunas = regionRepository.getRegiones().find { it.nombre == usuario.region }?.comunas ?: emptyList()
        )
        showEditUserDialog = true // Muestra el diálogo de edición.
    }

    fun onEditUserDismiss() { // Cierra el diálogo de edición.
        showEditUserDialog = false
        editUiState = RegistroUiState() // Reinicia el estado de edición.
    }

    fun onEditUserSuccessDialogDismiss() { // Cierra el diálogo de éxito de edición.
        showEditUserSuccessDialog = false
    }

    // --- Funciones para actualizar los campos del formulario de registro ---
    fun onRunChange(value: String) { registroUiState = registroUiState.copy(run = value, error = null) }
    fun onNombreChange(value: String) { registroUiState = registroUiState.copy(nombre = value, error = null) }
    fun onApellidosChange(value: String) { registroUiState = registroUiState.copy(apellidos = value, error = null) }
    fun onCorreoChange(value: String) { registroUiState = registroUiState.copy(correo = value, error = null) }
    fun onContrasenhaChange(value: String) { registroUiState = registroUiState.copy(contrasenha = value, error = null) }
    fun onConfirmContrasenhaChange(value: String) { registroUiState = registroUiState.copy(confirmContrasenha = value, error = null) }
    fun onTelefonoChange(value: String) { registroUiState = registroUiState.copy(telefono = value, error = null) }
    fun onFechaNacimientoChange(value: String) { registroUiState = registroUiState.copy(fechaNacimiento = value, error = null) }
    fun onRegionChange(value: String) {
        val comunas = registroUiState.regiones.find { it.nombre == value }?.comunas ?: emptyList()
        registroUiState = registroUiState.copy(region = value, comuna = "", comunas = comunas, error = null) // Actualiza la región y resetea la comuna.
    }
    fun onComunaChange(value: String) { registroUiState = registroUiState.copy(comuna = value, error = null) }
    fun onDireccionChange(value: String) { registroUiState = registroUiState.copy(direccion = value, error = null) }
    fun onTipoChange(value: String) { registroUiState = registroUiState.copy(tipo = value, error = null) }

    // --- Funciones para actualizar los campos del formulario de edición ---
    fun onEditNombreChange(value: String) { editUiState = editUiState.copy(nombre = value, error = null) }
    fun onEditApellidosChange(value: String) { editUiState = editUiState.copy(apellidos = value, error = null) }
    fun onEditCorreoChange(value: String) { editUiState = editUiState.copy(correo = value, error = null) }
    fun onEditContrasenhaChange(value: String) { editUiState = editUiState.copy(contrasenha = value, error = null) }
    fun onEditConfirmContrasenhaChange(value: String) { editUiState = editUiState.copy(confirmContrasenha = value, error = null) }
    fun onEditTelefonoChange(value: String) { editUiState = editUiState.copy(telefono = value, error = null) }
    fun onEditFechaNacimientoChange(value: String) { editUiState = editUiState.copy(fechaNacimiento = value, error = null) }
    fun onEditRegionChange(value: String) {
        val comunas = editUiState.regiones.find { it.nombre == value }?.comunas ?: emptyList()
        editUiState = editUiState.copy(region = value, comuna = "", comunas = comunas, error = null) // Actualiza la región y resetea la comuna.
    }
    fun onEditComunaChange(value: String) { editUiState = editUiState.copy(comuna = value, error = null) }
    fun onEditDireccionChange(value: String) { editUiState = editUiState.copy(direccion = value, error = null) }
    fun onEditTipoChange(value: String) { editUiState = editUiState.copy(tipo = value, error = null) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submit() { // Envía el formulario de registro.
        viewModelScope.launch {
            registroUiState = registroUiState.copy(error = null) // Limpia errores previos.

            // Validaciones de los campos.
            if (authRepository.isRunRegistered(registroUiState.run)) {
                registroUiState = registroUiState.copy(error = "El RUN ya se encuentra registrado.")
                return@launch
            }
            if (authRepository.isEmailRegistered(registroUiState.correo)) {
                registroUiState = registroUiState.copy(error = "El correo ya se encuentra registrado.")
                return@launch
            }
            if (!isValidRun(registroUiState.run)) {
                registroUiState = registroUiState.copy(error = "El RUN no es válido.")
                return@launch
            }
            if (registroUiState.nombre.trim().length < 3) {
                registroUiState = registroUiState.copy(error = "El nombre debe tener al menos 3 caracteres.")
                return@launch
            }
            if (registroUiState.apellidos.trim().length < 3) {
                registroUiState = registroUiState.copy(error = "Los apellidos deben tener al menos 3 caracteres.")
                return@launch
            }
            if (!isValidEmail(registroUiState.correo)) {
                registroUiState = registroUiState.copy(error = "El correo debe ser @duoc.cl, @profesor.duoc.cl o @gmail.com.")
                return@launch
            }
            if (registroUiState.contrasenha.length !in 6..10) {
                registroUiState = registroUiState.copy(error = "La contraseña debe tener entre 6 y 10 caracteres.")
                return@launch
            }
            if (registroUiState.contrasenha != registroUiState.confirmContrasenha) {
                registroUiState = registroUiState.copy(error = "Las contraseñas no coinciden.")
                return@launch
            }
            if (registroUiState.telefono.isNotEmpty() && !isValidPhone(registroUiState.telefono)) {
                registroUiState = registroUiState.copy(error = "El teléfono debe tener 9 dígitos y comenzar con 9.")
                return@launch
            }
            if (!isOfLegalAge(registroUiState.fechaNacimiento)) {
                registroUiState = registroUiState.copy(error = "Debes tener al menos 10 años para registrarte.")
                return@launch
            }
            if (registroUiState.direccion.trim().length < 3) {
                registroUiState = registroUiState.copy(error = "La dirección debe tener al menos 3 caracteres.")
                return@launch
            }

            val newUser = Usuario( // Crea un nuevo objeto Usuario con los datos del formulario.
                run = registroUiState.run,
                nombre = registroUiState.nombre,
                apellidos = registroUiState.apellidos,
                correo = registroUiState.correo,
                contrasenha = registroUiState.contrasenha,
                telefono = registroUiState.telefono,
                fechaNacimiento = registroUiState.fechaNacimiento,
                tipo = registroUiState.tipo,
                region = registroUiState.region,
                comuna = registroUiState.comuna,
                direccion = registroUiState.direccion
            )
            authRepository.registrar(newUser) // Llama al repositorio para registrar el nuevo usuario.
            onAddUserDismiss() // Cierra el diálogo de registro.
            showAddUserSuccessDialog = true // Muestra el diálogo de éxito.
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submitEdit() { // Envía el formulario de edición.
        viewModelScope.launch {
            editUiState = editUiState.copy(error = null) // Limpia errores previos.

            // Validaciones de los campos.
            if (editUiState.nombre.trim().length < 3) {
                editUiState = editUiState.copy(error = "El nombre debe tener al menos 3 caracteres.")
                return@launch
            }
            if (editUiState.apellidos.trim().length < 3) {
                editUiState = editUiState.copy(error = "Los apellidos deben tener al menos 3 caracteres.")
                return@launch
            }
            if (!isValidEmail(editUiState.correo)) {
                editUiState = editUiState.copy(error = "El correo debe ser @duoc.cl, @profesor.duoc.cl o @gmail.com.")
                return@launch
            }
            if (editUiState.contrasenha.isNotEmpty() && editUiState.contrasenha.length !in 6..10) {
                editUiState = editUiState.copy(error = "La contraseña debe tener entre 6 y 10 caracteres.")
                return@launch
            }
            if (editUiState.contrasenha != editUiState.confirmContrasenha) {
                editUiState = editUiState.copy(error = "Las contraseñas no coinciden.")
                return@launch
            }
            if (editUiState.telefono.isNotEmpty() && !isValidPhone(editUiState.telefono)) {
                editUiState = editUiState.copy(error = "El teléfono debe tener 9 dígitos y comenzar con 9.")
                return@launch
            }
            if (!isOfLegalAge(editUiState.fechaNacimiento)) {
                editUiState = editUiState.copy(error = "Debes tener al menos 10 años para registrarte.")
                return@launch
            }
            if (editUiState.direccion.trim().length < 3) {
                editUiState = editUiState.copy(error = "La dirección debe tener al menos 3 caracteres.")
                return@launch
            }

            val updatedUser = Usuario( // Crea un objeto Usuario con los datos actualizados.
                run = editUiState.run,
                nombre = editUiState.nombre,
                apellidos = editUiState.apellidos,
                correo = editUiState.correo,
                contrasenha = if (editUiState.contrasenha.isNotEmpty()) editUiState.contrasenha else authRepository.getUsuarioPorRut(editUiState.run)!!.contrasenha, // Si la contraseña está vacía, mantiene la original.
                telefono = editUiState.telefono,
                fechaNacimiento = editUiState.fechaNacimiento,
                tipo = editUiState.tipo,
                region = editUiState.region,
                comuna = editUiState.comuna,
                direccion = editUiState.direccion
            )
            authRepository.actualizarUsuario(updatedUser) // Llama al repositorio para actualizar el usuario.
            onEditUserDismiss() // Cierra el diálogo de edición.
            showEditUserSuccessDialog = true // Muestra el diálogo de éxito.
        }
    }

    private fun isValidRun(run: String): Boolean { // Valida el formato del RUN chileno.
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

    private fun isValidEmail(email: String): Boolean { // Valida que el dominio del correo sea uno de los permitidos.
        val validDomains = listOf("@duoc.cl", "@profesor.duoc.cl", "@gmail.com")
        return validDomains.any { email.endsWith(it) }
    }

    private fun isValidPhone(phone: String): Boolean { // Valida el formato del número de teléfono.
        if (phone.length != 9) return false
        if (phone.first() != '9') return false
        return phone.all { it.isDigit() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isOfLegalAge(birthDate: String): Boolean { // Valida que el usuario sea mayor de 10 años.
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

data class RegistroUiState( // Clase de datos que representa el estado del formulario de registro/edición.
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
    val tipo: String = "Cliente",
    val regiones: List<Region> = emptyList(),
    val comunas: List<String> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

class AdminUsuariosViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminUsuariosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminUsuariosViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}