package com.ct.junimostoreapp.data.repository

import com.ct.junimostoreapp.data.dao.OrdenDao
import com.ct.junimostoreapp.data.model.Orden
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot

/**
 * Prueba unitaria para OrdenRepository.
 *
 * PROPÓSITO PRINCIPAL:
 * Validar que la lógica de negocio para la creación de órdenes, especialmente la generación
 * de números de orden secuenciales, funciona correctamente.
 */
class OrdenRepositoryTest : FunSpec({

    lateinit var mockOrdenDao: OrdenDao
    lateinit var repository: OrdenRepository

    beforeTest {
        mockOrdenDao = mockk(relaxed = true)
        repository = OrdenRepository(mockOrdenDao)
    }

    test("crearOrden debe generar el número 'SO1001' si es la primera orden") {
        // 1. Preparación (Arrange)
        // Simulamos que la base de datos no tiene órdenes previas
        coEvery { mockOrdenDao.getUltimoNumeroOrden() } returns null

        // Creamos un "slot" para capturar el objeto Orden que se pasa al DAO
        val ordenSlot = slot<Orden>()
        coEvery { mockOrdenDao.insertOrden(capture(ordenSlot)) } returns Unit

        // 2. Acción (Act)
        repository.crearOrden("11111111-1", emptyList(), 5000)

        // 3. Verificación (Assert)
        // Verificamos que la orden capturada tenga el número de orden esperado
        ordenSlot.captured.numeroOrden shouldBe "SO1001"
    }

    test("crearOrden debe generar un número de orden secuencial si ya existen órdenes") {
        // 1. Preparación (Arrange)
        // Simulamos que la última orden en la base de datos es "SO1026"
        coEvery { mockOrdenDao.getUltimoNumeroOrden() } returns "SO1026"

        val ordenSlot = slot<Orden>()
        coEvery { mockOrdenDao.insertOrden(capture(ordenSlot)) } returns Unit

        // 2. Acción (Act)
        repository.crearOrden("22222222-2", emptyList(), 9990)

        // 3. Verificación (Assert)
        // Verificamos que la nueva orden sea la "SO1027"
        ordenSlot.captured.numeroOrden shouldBe "SO1027"
        // También verificamos que se llamó a la función para obtener el último número
        coVerify { mockOrdenDao.getUltimoNumeroOrden() }
    }
})
