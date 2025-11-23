package com.ct.junimostoreapp

import android.app.Application
import com.ct.junimostoreapp.data.dao.OrdenDao
import com.ct.junimostoreapp.data.database.OrdenDatabase
import com.ct.junimostoreapp.data.database.ProductoDatabase
import com.ct.junimostoreapp.data.database.UsuarioDatabase
import com.ct.junimostoreapp.data.repository.AuthRepository
import com.ct.junimostoreapp.data.repository.ProductoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ProductoApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val productoDatabase by lazy { ProductoDatabase.getDatabase(this, applicationScope) }
    val productoRepository by lazy { ProductoRepository(productoDatabase.productoDao()) }

    private val usuarioDatabase by lazy { UsuarioDatabase.getDatabase(this, applicationScope) }
    val authRepository by lazy { AuthRepository(usuarioDatabase.usuarioDao()) }

    private val ordenDatabase by lazy { OrdenDatabase.getDatabase(this) }
    val ordenDao: OrdenDao by lazy { ordenDatabase.ordenDao() }
}