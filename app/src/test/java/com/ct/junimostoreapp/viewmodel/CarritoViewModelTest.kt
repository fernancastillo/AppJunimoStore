package com.ct.junimostoreapp.viewmodel

import com.ct.junimostoreapp.data.model.Producto
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.OrdenRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

/**
 * Prueba unitaria para CarritoViewModel.
 *
 * PROPÓSITO PRINCIPAL:
 * Validar que la lógica de negocio del carrito (añadir, aumentar cantidad) funciona correctamente
 * de forma aislada, utilizando mocks para las dependencias externas (repositorios).
 */
class CarritoViewModelTest : FunSpec({

    // Mocks para las dependencias del ViewModel
    lateinit var mockProductoRepository: ProductoRepository
    lateinit var mockAuthRepository: AuthRepository
    lateinit var mockOrdenRepository: OrdenRepository

    // El ViewModel que vamos a probar
    lateinit var viewModel: CarritoViewModel

    // El `beforeTest` se ejecuta antes de cada test
    beforeTest {
        // Creamos mocks frescos para cada prueba
        mockProductoRepository = mockk(relaxed = true)
        mockAuthRepository = mockk(relaxed = true)
        mockOrdenRepository = mockk(relaxed = true)

        // Creamos una nueva instancia del ViewModel con los mocks
        viewModel = CarritoViewModel(
            productoRepository = mockProductoRepository,
            authRepository = mockAuthRepository,
            ordenRepository = mockOrdenRepository
        )
    }

    test("añadir un nuevo producto al carrito debe agregarlo a la lista") {
        // 1. Preparación (Arrange)
        val productoNuevo = Producto(
            codigo = "P001",
            nombre = "Test Product",
            precio = 1000,
            stock = 10,
            imagen = 0,
            categoria = "Test",
            descripcion = "",
            stockCritico = 5
        )
        viewModel.cartItems.size shouldBe 0 // Verificación inicial: el carrito está vacío

        // 2. Acción (Act)
        viewModel.addToCart(productoNuevo, 2)

        // 3. Verificación (Assert)
        viewModel.cartItems.size shouldBe 1
        viewModel.cartItems[0].product.codigo shouldBe "P001"
        viewModel.cartItems[0].quantity shouldBe 2
        viewModel.cartItems[0].total shouldBe 2000
    }

    test("añadir un producto existente debe aumentar la cantidad y el total") {
        // 1. Preparación (Arrange)
        val productoExistente = Producto(
            codigo = "P001",
            nombre = "Test Product",
            precio = 1000,
            stock = 10,
            imagen = 0,
            categoria = "Test",
            descripcion = "",
            stockCritico = 5
        )
        viewModel.addToCart(productoExistente, 1) // Añadimos 1 unidad primero

        viewModel.cartItems.size shouldBe 1 // Verificación inicial
        viewModel.cartItems[0].quantity shouldBe 1
        viewModel.cartItems[0].total shouldBe 1000

        // 2. Acción (Act)
        viewModel.addToCart(productoExistente, 2) // Añadimos 2 unidades más del MISMO producto

        // 3. Verificación (Assert)
        viewModel.cartItems.size shouldBe 1 // El tamaño de la lista no debe cambiar
        viewModel.cartItems[0].quantity shouldBe 3 // La cantidad debe ser 1 + 2 = 3
        viewModel.cartItems[0].total shouldBe 3000 // El total debe ser 3 * 1000 = 3000
    }
})
