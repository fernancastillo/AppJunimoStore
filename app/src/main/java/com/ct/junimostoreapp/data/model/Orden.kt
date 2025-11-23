package com.ct.junimostoreapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ordenes")
data class Orden( 
    @PrimaryKey
    val numeroOrden: String, 
    val fecha: String, 
    val run: String, 
    val estadoEnvio: String, 
    val total: Int
)
