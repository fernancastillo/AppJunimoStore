package com.ct.junimostoreapp.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos") // Anotación que marca esta clase como una tabla en la base de datos de Room.
data class Producto( // Define una clase de datos para representar un producto.
    @PrimaryKey val codigo: String, // Clave primaria de la tabla, identificador único del producto.
    val categoria: String, // Categoría a la que pertenece el producto.
    val nombre: String, // Nombre del producto.
    val descripcion: String, // Descripción detallada del producto.
    val precio: Int, // Precio del producto.
    var stock: Int, // Cantidad de unidades disponibles en el inventario.
    val stockCritico: Int, // Nivel de stock a partir del cual se considera que las existencias son bajas.
    @DrawableRes val imagen: Int // Recurso de imagen para mostrar el producto.
)
