package com.ct.junimostoreapp.data.model

import com.ct.junimostoreapp.data.model.Producto

data class CartItem(
    val product: Producto,
    val quantity: Int,
    val total: Int
)
