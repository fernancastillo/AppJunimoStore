package com.ct.junimostoreapp.data.model

data class Credential (val username:String,val password:String){ // Define una clase de datos para almacenar las credenciales (nombre de usuario y contraseña).
    // objeto para acceder a la instancia
    companion object{ // Define un objeto compañero que contiene miembros estáticos para la clase Credential.
        val Admin= Credential(username="admin", password = "123") // Crea una instancia estática de Credential para un usuario administrador.
    }

}//fin data