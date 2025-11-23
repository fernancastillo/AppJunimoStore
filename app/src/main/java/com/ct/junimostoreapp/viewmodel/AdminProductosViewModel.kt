package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.model.Producto
import com.ct.junimostoreapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AdminProductosViewModel(private val repository: ProductoRepository) : ViewModel() { // ViewModel para la pantalla de administración de productos.

    // Flujo de estado mutable para la lista de productos. Es privado para que solo el ViewModel pueda modificarlo.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    // Flujo de estado inmutable expuesto a la UI para observar los cambios en la lista de productos.
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // Lista inmutable de categorías de productos para el menú desplegable.
    val categorias = listOf("Accesorios", "Decoración", "Guías", "Juego de Mesa", "Mods Digitales", "Peluches", "Polera Personalizada")

    // Estado para controlar la visibilidad del diálogo de confirmación de eliminación.
    var showConfirmDialog by mutableStateOf(false)
        private set // Solo el ViewModel puede cambiar este valor.

    // Estado para controlar la visibilidad del diálogo de éxito después de eliminar.
    var showSuccessDialog by mutableStateOf(false)
        private set

    // Almacena el producto seleccionado para alguna acción (eliminar, editar).
    var selectedProducto by mutableStateOf<Producto?>(null)
        private set

    // Estado para controlar la visibilidad del diálogo para agregar un nuevo producto.
    var showAddProductDialog by mutableStateOf(false)
        private set

    // Estado para controlar la visibilidad del diálogo de éxito después de agregar un producto.
    var showAddSuccessDialog by mutableStateOf(false)
        private set

    // Estado para controlar la visibilidad del diálogo para editar un producto.
    var showEditProductDialog by mutableStateOf(false)
        private set

    // Estado para controlar la visibilidad del diálogo de éxito después de editar un producto.
    var showEditSuccessDialog by mutableStateOf(false)
        private set

    // Almacena el estado del producto que se está creando.
    var newProductoState by mutableStateOf(Producto("", "", "", "", 0, 0, 0, R.drawable.junimo))
        private set

    // Almacena el estado del producto que se está editando.
    var editProductoState by mutableStateOf<Producto?>(null)

    // Almacena un mensaje de error de validación, si existe.
    var error by mutableStateOf<String?>(null)
        private set

    init { // Bloque de inicialización del ViewModel.
        loadProductos() // Carga la lista inicial de productos.
    }

    private fun loadProductos() { // Función privada para cargar los productos desde el repositorio.
        viewModelScope.launch { // Inicia una corutina en el ciclo de vida del ViewModel.
            repository.getProductos().catch { e ->
                // Manejar el error
            }.collect {
                _productos.value = it
            }
        }
    }

    fun onEliminarClick(producto: Producto) { // Se llama al hacer clic en el botón de eliminar de un producto.
        selectedProducto = producto // Guarda el producto seleccionado.
        showConfirmDialog = true // Muestra el diálogo de confirmación.
    }

    fun onConfirmEliminar() { // Se llama al confirmar la eliminación en el diálogo.
        viewModelScope.launch {
            selectedProducto?.let { // Si hay un producto seleccionado...
                repository.removeProducto(it) // ...lo elimina del repositorio.
            }
            showConfirmDialog = false // Oculta el diálogo de confirmación.
            showSuccessDialog = true // Muestra el diálogo de éxito.
        }
    }

    fun onDialogDismiss() { // Se llama para cerrar los diálogos de eliminación.
        showConfirmDialog = false // Oculta el diálogo de confirmación.
        showSuccessDialog = false // Oculta el diálogo de éxito.
        selectedProducto = null // Limpia la selección.
    }

    fun onAddProductClick() { // Se llama al hacer clic en el botón para agregar un producto.
        newProductoState = Producto("", "", "", "", 0, 0, 0, R.drawable.junimo) // Reinicia el estado para un nuevo producto.
        error = null // Limpia errores previos.
        showAddProductDialog = true // Muestra el diálogo para agregar.
    }

    fun onAddProductDismiss() { // Se llama para cerrar el diálogo de agregar producto.
        showAddProductDialog = false // Oculta el diálogo.
    }

    fun onAddSuccessDialogDismiss() { // Se llama para cerrar el diálogo de éxito después de agregar.
        showAddSuccessDialog = false // Oculta el diálogo.
    }

    fun onEditProductClick(producto: Producto) { // Se llama al hacer clic en el botón de editar.
        editProductoState = producto // Guarda el estado del producto a editar.
        error = null // Limpia errores previos.
        showEditProductDialog = true // Muestra el diálogo de edición.
    }

    fun onEditProductDismiss() { // Se llama para cerrar el diálogo de edición.
        showEditProductDialog = false // Oculta el diálogo.
    }

    fun onEditSuccessDialogDismiss() { // Se llama para cerrar el diálogo de éxito después de editar.
        showEditSuccessDialog = false // Oculta el diálogo.
    }

    // --- Funciones para actualizar los campos del formulario de nuevo producto ---
    fun onNewCodigoChange(value: String) {
        newProductoState = newProductoState.copy(codigo = value)
        error = null
    }

    fun onNewCategoriaChange(value: String) {
        newProductoState = newProductoState.copy(categoria = value)
        error = null
    }

    fun onNewNombreChange(value: String) {
        newProductoState = newProductoState.copy(nombre = value)
        error = null
    }

    fun onNewDescripcionChange(value: String) {
        newProductoState = newProductoState.copy(descripcion = value)
        error = null
    }

    fun onNewPrecioChange(value: String) {
        newProductoState = newProductoState.copy(precio = value.toIntOrNull() ?: 0)
        error = null
    }

    fun onNewStockChange(value: String) {
        newProductoState = newProductoState.copy(stock = value.toIntOrNull() ?: 0)
        error = null
    }

    fun onNewStockCriticoChange(value: String) {
        newProductoState = newProductoState.copy(stockCritico = value.toIntOrNull() ?: 0)
        error = null
    }

    // --- Funciones para actualizar los campos del formulario de edición de producto ---
    fun onEditCategoriaChange(value: String) {
        editProductoState = editProductoState?.copy(categoria = value)
        error = null
    }

    fun onEditNombreChange(value: String) {
        editProductoState = editProductoState?.copy(nombre = value)
        error = null
    }

    fun onEditDescripcionChange(value: String) {
        editProductoState = editProductoState?.copy(descripcion = value)
        error = null
    }

    fun onEditPrecioChange(value: String) {
        editProductoState = editProductoState?.copy(precio = value.toIntOrNull() ?: 0)
        error = null
    }

    fun onEditStockChange(value: String) {
        editProductoState = editProductoState?.copy(stock = value.toIntOrNull() ?: 0)
        error = null
    }

    fun onEditStockCriticoChange(value: String) {
        editProductoState = editProductoState?.copy(stockCritico = value.toIntOrNull() ?: 0)
        error = null
    }

    fun submitNewProducto() { // Se llama para guardar el nuevo producto.
        viewModelScope.launch {
            if (validate(newProductoState)) { // Si la validación es exitosa...
                repository.addProducto(newProductoState) // ...agrega el producto al repositorio.
                onAddProductDismiss() // Cierra el diálogo de agregar.
                showAddSuccessDialog = true // Muestra el diálogo de éxito.
            }
        }
    }

    fun submitEditProducto() { // Se llama para guardar los cambios del producto editado.
        viewModelScope.launch {
            editProductoState?.let { // Si hay un producto en edición...
                if (validate(it)) { // ...y la validación es exitosa...
                    repository.updateProducto(it) // ...actualiza el producto en el repositorio.
                    onEditProductDismiss() // Cierra el diálogo de edición.
                    showEditSuccessDialog = true // Muestra el diálogo de éxito.
                }
            }
        }
    }

    private fun validate(producto: Producto): Boolean { // Función de validación para los campos del producto.
        if (producto.codigo.length <= 3) {
            error = "El código debe tener más de 3 caracteres."
            return false
        }
        if (producto.nombre.length <= 3) {
            error = "El nombre debe tener más de 3 caracteres."
            return false
        }
        if (producto.categoria.isEmpty()) {
            error = "Debes seleccionar una categoría."
            return false
        }
        if (producto.descripcion.length <= 3) {
            error = "La descripción debe tener más de 3 caracteres."
            return false
        }
        if (producto.precio < 0) {
            error = "El precio debe ser mayor o igual a 0."
            return false
        }
        if (producto.stock < 0) {
            error = "El stock debe ser mayor o igual a 0."
            return false
        }
        if (producto.stockCritico < 0) {
            error = "El stock crítico debe ser mayor o igual a 0."
            return false
        }
        return true // Si todas las validaciones pasan, devuelve verdadero.
    }
}

class AdminProductosViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminProductosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}