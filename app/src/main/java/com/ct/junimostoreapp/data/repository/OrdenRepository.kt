package com.ct.junimostoreapp.data.repository

import com.ct.junimostoreapp.data.dao.OrdenDao
import com.ct.junimostoreapp.data.model.Orden
import com.ct.junimostoreapp.data.model.OrdenConProductos
import com.ct.junimostoreapp.data.model.ProductoOrden
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdenRepository(private val ordenDao: OrdenDao) {
    suspend fun crearOrden(rut: String, productos: List<ProductoOrden>, total: Int) {
        val ultimoNumeroOrden = ordenDao.getUltimoNumeroOrden()
        val nuevoNumero = if (ultimoNumeroOrden == null) {
            1001 // Es la primera orden
        } else {
            val numero = ultimoNumeroOrden.removePrefix("SO").toIntOrNull() ?: 1000
            numero + 1
        }

        val numeroOrdenFormateado = "SO$nuevoNumero"
        val fecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val orden = Orden(
            numeroOrden = numeroOrdenFormateado,
            run = rut,
            fecha = fecha,
            total = total,
            estadoEnvio = "En preparación"
        )
        val productosConNumeroOrden = productos.map { it.copy(ordenNumeroOrden = numeroOrdenFormateado) }

        ordenDao.insertOrden(orden)
        ordenDao.insertProductosOrden(productosConNumeroOrden)
    }

    fun getOrdenesPorRun(run: String): Flow<List<OrdenConProductos>> {
        return ordenDao.getOrdenesConProductosPorRun(run)
    }

    fun getOrdenesConProductos(): Flow<List<OrdenConProductos>> {
        return ordenDao.getOrdenesConProductos()
    }

    suspend fun removeOrden(orden: Orden) {
        ordenDao.deleteOrden(orden)
    }

    suspend fun updateOrden(orden: Orden) {
        ordenDao.updateOrden(orden)
    }

    suspend fun getTotalOrdenes(): Int = ordenDao.getTotalOrdenes()
    suspend fun getOrdenesPendientes(): Int = ordenDao.getOrdenesCountByEstado("Pendiente")
    suspend fun getOrdenesEnviadas(): Int = ordenDao.getOrdenesCountByEstado("Enviado")
    suspend fun getOrdenesEntregadas(): Int = ordenDao.getOrdenesCountByEstado("Entregado")
    suspend fun getOrdenesCanceladas(): Int = ordenDao.getOrdenesCountByEstado("Cancelado")
}
