package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.Producto
import com.ct.junimostoreapp.data.model.ProductoOrden
import com.ct.junimostoreapp.data.model.Resena
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModel() {

    var filtroNombre by mutableStateOf("")
        private set

    var filtroCategoria by mutableStateOf("Todas")
        private set

    private val _allProductos = MutableStateFlow<List<Producto>>(emptyList())

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _producto = MutableStateFlow<Producto?>(null)
    val producto: StateFlow<Producto?> = _producto.asStateFlow()

    private val _resenas = MutableStateFlow<List<Resena>>(emptyList())
    val resenas: StateFlow<List<Resena>> = _resenas.asStateFlow()

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    init {
        viewModelScope.launch {
            _categorias.value = listOf("Todas") + productoRepository.getCategorias()

            _resenas.value = listOf(
                Resena(titulo = "¡Fantástico!", texto = "El producto superó mis expectativas. La calidad es increíble y el diseño es muy fiel al juego. ¡Totalmente recomendado para cualquier fan!", calificacion = 5),
                Resena(titulo = "Buen producto, pero...", texto = "Es un buen producto en general, pero algunos detalles podrían mejorar. El material se siente un poco frágil. Cumple su función, pero no es perfecto.", calificacion = 3),
                Resena(titulo = "No fue lo que esperaba", texto = "Lamentablemente, el producto no se parece mucho al de las fotos. La calidad es bastante baja y no creo que valga la pena por el precio. Una decepción.", calificacion = 1)
            )

            productoRepository.getProductos().catch { e ->
                // Manejar error
            }.collect {
                _allProductos.value = it
                _productos.value = it
            }
        }
    }

    fun onFiltroNombreChange(nombre: String) {
        filtroNombre = nombre
        filtrarProductos()
    }

    fun onFiltroCategoriaChange(categoria: String) {
        filtroCategoria = categoria
        filtrarProductos()
    }

    private fun filtrarProductos() {
        var productosFiltrados = _allProductos.value

        if (filtroNombre.isNotBlank()) {
            productosFiltrados = productosFiltrados.filter {
                it.nombre.contains(filtroNombre, ignoreCase = true)
            }
        }

        if (filtroCategoria != "Todas") {
            productosFiltrados = productosFiltrados.filter { it.categoria == filtroCategoria }
        }

        _productos.value = productosFiltrados
    }

    fun getProducto(codigo: String, rut: String) {
        viewModelScope.launch {
            _producto.value = productoRepository.getProductoPorCodigo(codigo)
        }
    }

    fun reducirStock(codigo: String, cantidad: Int) {
        viewModelScope.launch {
            val producto = productoRepository.getProductoPorCodigo(codigo)
            producto?.let {
                val nuevoStock = it.stock - cantidad
                productoRepository.actualizarStock(codigo, nuevoStock)
            }
        }
    }
}

class ProductoViewModelFactory(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(productoRepository, authRepository, ordenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
