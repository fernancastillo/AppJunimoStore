package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.Orden
import com.ct.junimostoreapp.data.model.OrdenConProductos
import com.ct.junimostoreapp.data.repository.OrdenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AdminOrdenesViewModel(private val ordenRepository: OrdenRepository) : ViewModel() {

    private val _ordenes = MutableStateFlow<List<OrdenConProductos>>(emptyList())
    val ordenes: StateFlow<List<OrdenConProductos>> = _ordenes.asStateFlow()

    val estadosEnvio = listOf("Pendiente", "Enviado", "Entregado", "Cancelado")

    var showConfirmDialog by mutableStateOf(false)
        private set

    var showSuccessDialog by mutableStateOf(false)
        private set

    var selectedOrden by mutableStateOf<Orden?>(null)
        private set

    var showEditOrdenDialog by mutableStateOf(false)
        private set

    var showEditSuccessDialog by mutableStateOf(false)
        private set

    var editOrdenState by mutableStateOf<Orden?>(null)

    var error by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            ordenRepository.getOrdenesConProductos()
                .distinctUntilChanged()
                .collect { listaDeOrdenes ->
                    _ordenes.value = listaDeOrdenes
                }
        }
    }

    fun onEliminarClick(orden: Orden) {
        selectedOrden = orden
        showConfirmDialog = true
    }

    fun onConfirmEliminar() {
        viewModelScope.launch {
            selectedOrden?.let {
                ordenRepository.removeOrden(it)
            }
            showConfirmDialog = false
            showSuccessDialog = true
        }
    }

    fun onDialogDismiss() {
        showConfirmDialog = false
        showSuccessDialog = false
        selectedOrden = null
    }

    fun onEditOrdenClick(orden: Orden) {
        editOrdenState = orden
        error = null
        showEditOrdenDialog = true
    }

    fun onEditOrdenDismiss() {
        showEditOrdenDialog = false
    }

    fun onEditSuccessDialogDismiss() {
        showEditSuccessDialog = false
    }

    fun onEditEstadoEnvioChange(value: String) {
        editOrdenState = editOrdenState?.copy(estadoEnvio = value)
        error = null
    }

    fun submitEditOrden() {
        editOrdenState?.let {
            if (it.estadoEnvio.isEmpty()) {
                error = "Debes seleccionar un estado."
                return@let
            }
            viewModelScope.launch {
                ordenRepository.updateOrden(it)
                onEditOrdenDismiss()
                showEditSuccessDialog = true
            }
        }
    }
}

class AdminOrdenesViewModelFactory(private val ordenRepository: OrdenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminOrdenesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminOrdenesViewModel(ordenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
