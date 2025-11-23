package com.ct.junimostoreapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resenas") // Anotación que marca esta clase como una tabla en la base de datos de Room.
data class Resena( // Define una clase de datos para representar una reseña de un producto.
    @PrimaryKey(autoGenerate = true) // Clave primaria autoincremental.
    val id: Int = 0,
    val titulo: String, // Título de la reseña.
    val texto: String, // Contenido de la reseña.
    val calificacion: Int // Calificación asignada al producto, generalmente en una escala de 1 a 5.
)
