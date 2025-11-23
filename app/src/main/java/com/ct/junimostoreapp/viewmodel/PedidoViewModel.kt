package com.ct.junimostoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.OrdenConProductos
import com.ct.junimostoreapp.data.repository.OrdenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PedidoViewModel(private val ordenRepository: OrdenRepository) : ViewModel() {
    private val _pedidos = MutableStateFlow<List<OrdenConProductos>>(emptyList())
    val pedidos: StateFlow<List<OrdenConProductos>> = _pedidos.asStateFlow()

    fun getPedidosPorRut(rut: String) {
        viewModelScope.launch {
            _pedidos.value = ordenRepository.getOrdenesPorRun(rut)
        }
    }
}

class PedidoViewModelFactory(private val ordenRepository: OrdenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PedidoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PedidoViewModel(ordenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}