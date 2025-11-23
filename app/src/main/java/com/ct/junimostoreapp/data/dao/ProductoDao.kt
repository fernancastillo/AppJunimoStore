package com.ct.junimostoreapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ct.junimostoreapp.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao // Anotación que marca esta interfaz como un Objeto de Acceso a Datos (DAO) para Room.
interface ProductoDao{

    // Inserta un nuevo producto. Si el producto ya existe (basado en la clave primaria), lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)

    // Obtiene todos los productos de la tabla y los devuelve como un Flow, lo que permite observar cambios en tiempo real.
    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<Producto>>

    // Obtiene un producto específico por su código.
    @Query("SELECT * FROM productos WHERE codigo = :codigo")
    suspend fun getProductoPorCodigo(codigo: String): Producto?

    // Actualiza un producto existente en la base de datos.
    @Update
    suspend fun updateProducto(producto: Producto)

    // Elimina un producto de la base de datos.
    @Delete
    suspend fun removeProducto(producto: Producto)

    // Obtiene una lista de todas las categorías de productos, sin duplicados.
    @Query("SELECT DISTINCT categoria FROM productos")
    suspend fun getCategorias(): List<String>

    // Obtiene el número total de productos.
    @Query("SELECT COUNT(*) FROM productos")
    suspend fun getTotalProductos(): Int

    // Obtiene el número de productos cuyo stock es menor o igual a su stock crítico.
    @Query("SELECT COUNT(*) FROM productos WHERE stock <= stockCritico")
    suspend fun getProductosConStockCritico(): Int

    // Obtiene el número de productos cuyo stock es cero.
    @Query("SELECT COUNT(*) FROM productos WHERE stock = 0")
    suspend fun getProductosSinStock(): Int
}
