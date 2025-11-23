package com.ct.junimostoreapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos_orden",
    foreignKeys = [ForeignKey(
        entity = Orden::class,
        parentColumns = ["numeroOrden"],
        childColumns = ["ordenNumeroOrden"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductoOrden(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var ordenNumeroOrden: String, 
    val codigo: String, 
    val nombre: String, 
    val cantidad: Int, 
    val precio: Int 
)
