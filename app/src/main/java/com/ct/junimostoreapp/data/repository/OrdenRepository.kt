package com.ct.junimostoreapp.data.repository

import com.ct.junimostoreapp.data.dao.OrdenDao
import com.ct.junimostoreapp.data.model.Orden
import com.ct.junimostoreapp.data.model.OrdenConProductos
import com.ct.junimostoreapp.data.model.ProductoOrden
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdenRepository(private val ordenDao: OrdenDao) {

    suspend fun getOrdenes(): List<OrdenConProductos> {
        return ordenDao.getOrdenesConProductos()
    }

    suspend fun getOrdenesPorRun(run: String): List<OrdenConProductos> {
        return ordenDao.getOrdenesConProductosPorRun(run)
    }

    suspend fun crearOrden(rut: String, productos: List<ProductoOrden>, total: Int) {
        val ultimoNumeroOrden = ordenDao.getOrdenesConProductos().lastOrNull()?.orden?.numeroOrden ?: "SO1000"
        val ultimoNumero = ultimoNumeroOrden.substring(2).toInt()
        val nuevoNumero = ultimoNumero + 1
        val nuevoNumeroOrden = "SO$nuevoNumero"

        val nuevaOrden = Orden(
            numeroOrden = nuevoNumeroOrden,
            fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
            run = rut,
            estadoEnvio = "Pendiente",
            total = total
        )
        ordenDao.insertOrden(nuevaOrden)

        val productosConIdDeOrden = productos.map {
            it.ordenNumeroOrden = nuevoNumeroOrden
            it
        }
        ordenDao.insertProductosOrden(productosConIdDeOrden)
    }

    suspend fun removeOrden(orden: Orden) {
        ordenDao.deleteOrden(orden)
    }

    suspend fun updateOrden(orden: Orden) {
        ordenDao.insertOrden(orden)
    }

    suspend fun getTotalOrdenes(): Int {
        return ordenDao.getTotalOrdenes()
    }

    suspend fun getOrdenesPendientes(): Int {
        return ordenDao.getOrdenesCountByEstado("Pendiente")
    }

    suspend fun getOrdenesEnviadas(): Int {
        return ordenDao.getOrdenesCountByEstado("Enviado")
    }

    suspend fun getOrdenesEntregadas(): Int {
        return ordenDao.getOrdenesCountByEstado("Entregado")
    }

    suspend fun getOrdenesCanceladas(): Int {
        return ordenDao.getOrdenesCountByEstado("Cancelado")
    }
}