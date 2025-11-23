package com.ct.junimostoreapp.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "regiones") // Anotación que marca esta clase como una tabla en la base de datos de Room.
data class Region( // Define una clase de datos para representar una región y sus comunas.
    @PrimaryKey // Clave primaria de la tabla, el nombre de la región es único.
    val nombre: String, // Nombre de la región.
    @Ignore // Room no puede almacenar listas directamente. @Ignore le dice a Room que no guarde este campo.
    val comunas: List<String> // Lista de comunas que pertenecen a la región.
)
