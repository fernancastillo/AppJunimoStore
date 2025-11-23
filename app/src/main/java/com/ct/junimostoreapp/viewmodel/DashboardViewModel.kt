package com.ct.junimostoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardStats(
    val totalProductos: Int = 0,
    val productosCriticos: Int = 0,
    val productosSinStock: Int = 0,
    val totalUsuarios: Int = 0,
    val admins: Int = 0,
    val clientes: Int = 0,
    val totalOrdenes: Int = 0,
    val ordenesPendientes: Int = 0,
    val ordenesEnviadas: Int = 0,
    val ordenesEntregadas: Int = 0,
    val ordenesCanceladas: Int = 0
)

class DashboardViewModel(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModel() {

    private val _stats = MutableStateFlow(DashboardStats())
    val stats = _stats.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            val newStats = DashboardStats(
                totalProductos = productoRepository.getTotalProductos(),
                productosCriticos = productoRepository.getProductosConStockCritico(),
                productosSinStock = productoRepository.getProductosSinStock(),
                totalUsuarios = authRepository.getTotalUsuarios(),
                admins = authRepository.getAdminCount(),
                clientes = authRepository.getClientCount(),
                totalOrdenes = ordenRepository.getTotalOrdenes(),
                ordenesPendientes = ordenRepository.getOrdenesPendientes(),
                ordenesEnviadas = ordenRepository.getOrdenesEnviadas(),
                ordenesEntregadas = ordenRepository.getOrdenesEntregadas(),
                ordenesCanceladas = ordenRepository.getOrdenesCanceladas()
            )
            _stats.value = newStats
        }
    }
}

class DashboardViewModelFactory(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(productoRepository, authRepository, ordenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}