package com.ct.junimostoreapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ct.junimostoreapp.data.model.CartItem
import com.ct.junimostoreapp.data.model.Producto
import com.ct.junimostoreapp.data.model.ProductoOrden
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CarritoViewModel(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
    private val ordenRepository: OrdenRepository
) : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    val productos: Flow<List<Producto>> = productoRepository.getProductos()

    var descuentoCodigo by mutableStateOf("")
        private set

    var descuentoAplicado by mutableStateOf(false)
        private set

    var descuentoMensaje by mutableStateOf<String?>(null)
        private set

    var descuentoDuoc by mutableStateOf(false)
        private set

    fun addToCart(product: Producto, quantity: Int) {
        val existingItem = _cartItems.find { it.product.codigo == product.codigo }
        if (existingItem != null) {
            val newQuantity = existingItem.quantity + quantity
            val newItem = existingItem.copy(
                quantity = newQuantity,
                total = newQuantity * product.precio
            )
            _cartItems[_cartItems.indexOf(existingItem)] = newItem
        } else {
            _cartItems.add(
                CartItem(
                    product = product,
                    quantity = quantity,
                    total = quantity * product.precio
                )
            )
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            val product = productoRepository.getProductoPorCodigo(item.product.codigo)
            product?.let {
                if (it.stock > 0) {
                    val newQuantity = item.quantity + 1
                    val newItem = item.copy(quantity = newQuantity, total = newQuantity * item.product.precio)
                    _cartItems[_cartItems.indexOf(item)] = newItem
                    productoRepository.actualizarStock(item.product.codigo, it.stock - 1)
                }
            }
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                val newQuantity = item.quantity - 1
                val newItem = item.copy(quantity = newQuantity, total = newQuantity * item.product.precio)
                _cartItems[_cartItems.indexOf(item)] = newItem
                val product = productoRepository.getProductoPorCodigo(item.product.codigo)
                product?.let {
                    productoRepository.actualizarStock(item.product.codigo, it.stock + 1)
                }
            }
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            _cartItems.remove(item)
            val product = productoRepository.getProductoPorCodigo(item.product.codigo)
            product?.let {
                productoRepository.actualizarStock(item.product.codigo, it.stock + item.quantity)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _cartItems.forEach { item ->
                val product = productoRepository.getProductoPorCodigo(item.product.codigo)
                product?.let {
                    productoRepository.actualizarStock(item.product.codigo, it.stock + item.quantity)
                }
            }
            _cartItems.clear()
        }
    }

    fun onDescuentoChange(codigo: String) {
        descuentoCodigo = codigo
        descuentoMensaje = null
        if (codigo.uppercase() != "SV1000") {
            descuentoAplicado = false
        }
    }

    fun aplicarDescuento() {
        if (descuentoCodigo.uppercase() == "SV1000") {
            descuentoAplicado = true
            descuentoMensaje = "Cupón agregado con éxito"
        } else {
            descuentoMensaje = "Cupón no existe"
        }
    }

    fun cancelarDescuento() {
        descuentoAplicado = false
        descuentoCodigo = ""
        descuentoMensaje = null
    }

    fun crearOrden(rut: String) {
        viewModelScope.launch {
            val productosOrden = _cartItems.map { 
                ProductoOrden(
                    ordenNumeroOrden = "",
                    codigo = it.product.codigo,
                    nombre = it.product.nombre,
                    cantidad = it.quantity,
                    precio = it.product.precio
                )
            }
            val total = _cartItems.sumOf { it.total }
            var totalConDescuento = if (descuentoAplicado) total - 1000 else total
            if (descuentoDuoc) {
                totalConDescuento = (totalConDescuento * 0.8).toInt()
            }
            ordenRepository.crearOrden(rut, productosOrden, totalConDescuento)
            _cartItems.clear()
        }
    }

    fun checkDuoc(rut: String) {
        viewModelScope.launch {
            val usuario = authRepository.getUsuarioPorRut(rut)
            if (usuario != null) {
                descuentoDuoc = usuario.correo.endsWith("@duoc.cl") || usuario.correo.endsWith("@profesor.duoc.cl")
            }
        }
    }
}