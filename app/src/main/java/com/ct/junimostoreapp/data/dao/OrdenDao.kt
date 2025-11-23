package com.ct.junimostoreapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ct.junimostoreapp.data.model.Orden
import com.ct.junimostoreapp.data.model.OrdenConProductos
import com.ct.junimostoreapp.data.model.ProductoOrden

@Dao
interface OrdenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrden(orden: Orden)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductosOrden(productos: List<ProductoOrden>)

    @Delete
    suspend fun deleteOrden(orden: Orden)

    @Transaction
    @Query("SELECT * FROM ordenes")
    suspend fun getOrdenesConProductos(): List<OrdenConProductos>

    @Transaction
    @Query("SELECT * FROM ordenes WHERE run = :run")
    suspend fun getOrdenesConProductosPorRun(run: String): List<OrdenConProductos>

    @Query("SELECT COUNT(*) FROM ordenes")
    suspend fun getTotalOrdenes(): Int

    @Query("SELECT COUNT(*) FROM ordenes WHERE estadoEnvio = :estado")
    suspend fun getOrdenesCountByEstado(estado: String): Int
}
