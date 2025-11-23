package com.ct.junimostoreapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios") // Anotación que marca esta clase como una tabla en la base de datos de Room.
data class Usuario( // Define una clase de datos para representar a un usuario de la aplicación.
    @PrimaryKey // Clave primaria de la tabla, el RUN es el identificador único.
    val run: String, // RUN del usuario, que también funciona como identificador único.
    val nombre: String, // Nombre del usuario.
    val apellidos: String, // Apellidos del usuario.
    val correo: String, // Correo electrónico del usuario.
    val contrasenha: String, // Contraseña del usuario para iniciar sesión.
    val telefono: String, // Número de teléfono del usuario.
    val fechaNacimiento: String, // Fecha de nacimiento del usuario.
    val tipo: String = "Cliente", // Tipo de usuario, con "Cliente" como valor por defecto.
    val region: String, // Región de residencia del usuario.
    val comuna: String, // Comuna de residencia del usuario.
    val direccion: String // Dirección de residencia del usuario.
)
