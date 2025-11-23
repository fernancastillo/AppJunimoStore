package com.ct.junimostoreapp.data.repository

import com.ct.junimostoreapp.data.dao.ProductoDao
import com.ct.junimostoreapp.data.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) { // Repositorio que gestiona los productos de la aplicación.

    // Obtiene todos los productos de la base de datos como un Flow.
    fun getProductos(): Flow<List<Producto>> = productoDao.obtenerProductos()

    // Obtiene un producto específico por su código.
    suspend fun getProductoPorCodigo(codigo: String): Producto? {
        return productoDao.getProductoPorCodigo(codigo)
    }

    // Actualiza el stock de un producto.
    suspend fun actualizarStock(codigo: String, nuevoStock: Int) {
        val producto = getProductoPorCodigo(codigo)
        producto?.let {
            it.stock = nuevoStock
            productoDao.updateProducto(it)
        }
    }

    // Obtiene una lista de todas las categorías de productos, sin duplicados.
    suspend fun getCategorias(): List<String> {
        return productoDao.getCategorias()
    }

    // Elimina un producto de la base de datos.
    suspend fun removeProducto(producto: Producto) {
        productoDao.removeProducto(producto)
    }

    // Agrega un nuevo producto a la base de datos.
    suspend fun addProducto(producto: Producto) {
        productoDao.insertarProducto(producto)
    }

    // Actualiza un producto existente en la base de datos.
    suspend fun updateProducto(producto: Producto) {
        productoDao.updateProducto(producto)
    }

    // Obtiene el número total de productos.
    suspend fun getTotalProductos(): Int = productoDao.getTotalProductos()

    // Obtiene el número de productos con stock crítico.
    suspend fun getProductosConStockCritico(): Int = productoDao.getProductosConStockCritico()

    // Obtiene el número de productos sin stock.
    suspend fun getProductosSinStock(): Int = productoDao.getProductosSinStock()
}