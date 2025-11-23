package com.ct.junimostoreapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class OrdenConProductos(
    @Embedded val orden: Orden,
    @Relation(
        parentColumn = "numeroOrden",
        entityColumn = "ordenNumeroOrden"
    )
    val productos: List<ProductoOrden>
)
