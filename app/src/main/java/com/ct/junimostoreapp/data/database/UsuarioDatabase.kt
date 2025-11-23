package com.ct.junimostoreapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ct.junimostoreapp.data.dao.UsuarioDao
import com.ct.junimostoreapp.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class UsuarioDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    private class UsuarioDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.usuarioDao())
                }
            }
        }

        suspend fun populateDatabase(usuarioDao: UsuarioDao) {
            val usuarios = listOf(
                Usuario(
                    run = "151131969",
                    nombre = "Luis",
                    apellidos = "Contreras Contreras",
                    correo = "admin@gmail.com",
                    contrasenha = "123456",
                    telefono = "985647852",
                    fechaNacimiento = "1985-08-12",
                    tipo = "Admin",
                    region = "Región Metropolitana de Santiago",
                    comuna = "Puente Alto",
                    direccion = "Las Casas 4352"
                ),
                Usuario(
                    run = "206947953",
                    nombre = "Marco",
                    apellidos = "Suazo Fuentes",
                    correo = "marco.suazo@duoc.cl",
                    contrasenha = "123321",
                    telefono = "965224865",
                    fechaNacimiento = "2000-04-05",
                    tipo = "Cliente",
                    region = "Región Metropolitana de Santiago",
                    comuna = "San Bernardo",
                    direccion = "El Pasaje 287"
                ),
                Usuario(
                    run = "220272430",
                    nombre = "Felipe",
                    apellidos = "Riquelme Acevedo",
                    correo = "felipe.riquelme@profesor.duoc.cl",
                    contrasenha = "123123",
                    telefono = "912345678",
                    fechaNacimiento = "2002-05-04",
                    tipo = "Cliente",
                    region = "Antofagasta",
                    comuna = "Mejillones",
                    direccion = "El Barrio 2362"
                ),
                Usuario(
                    run = "199022800",
                    nombre = "Roberto",
                    apellidos = "Zapata Espinoza",
                    correo = "roberto.zapata@duoc.cl",
                    contrasenha = "123333",
                    telefono = "935688744",
                    fechaNacimiento = "1998-11-20",
                    tipo = "Cliente",
                    region = "Magallanes y de la Antártica Chilena",
                    comuna = "Punta Arenas",
                    direccion = "El Vecindario 258"
                ),
                Usuario(
                    run = "215478967",
                    nombre = "Ana",
                    apellidos = "González Martínez",
                    correo = "ana.gonzalez@gmail.com",
                    contrasenha = "ana123",
                    telefono = "987654321",
                    fechaNacimiento = "2001-03-15",
                    tipo = "Cliente",
                    region = "Valparaíso",
                    comuna = "Viña del Mar",
                    direccion = "Av. San Martín 1234"
                ),
                Usuario(
                    run = "198745633",
                    nombre = "Carlos",
                    apellidos = "López Silva",
                    correo = "carlos.lopez@profesor.duoc.cl",
                    contrasenha = "carlos456",
                    telefono = "956321478",
                    fechaNacimiento = "1995-07-22",
                    tipo = "Cliente",
                    region = "Región Metropolitana de Santiago",
                    comuna = "Las Condes",
                    direccion = "Apquindo 4567"
                ),
                Usuario(
                    run = "225896341",
                    nombre = "María",
                    apellidos = "Silva Rojas",
                    correo = "maria.silva@duoc.cl",
                    contrasenha = "maria789",
                    telefono = "932145687",
                    fechaNacimiento = "2003-09-30",
                    tipo = "Cliente",
                    region = "Biobío",
                    comuna = "Concepción",
                    direccion = "O'Higgins 789"
                ),
                Usuario(
                    run = "187459632",
                    nombre = "Javier",
                    apellidos = "Muñoz Díaz",
                    correo = "javier.munoz@gmail.com",
                    contrasenha = "javier321",
                    telefono = "914785236",
                    fechaNacimiento = "1992-12-10",
                    tipo = "Cliente",
                    region = "Araucanía",
                    comuna = "Temuco",
                    direccion = "Manuel Montt 852"
                ),
                Usuario(
                    run = "210258740",
                    nombre = "Fernanda",
                    apellidos = "Riquelme Vargas",
                    correo = "fernanda.riquelme@duoc.cl",
                    contrasenha = "fer123",
                    telefono = "965874123",
                    fechaNacimiento = "2004-06-18",
                    tipo = "Cliente",
                    region = "Región Metropolitana de Santiago",
                    comuna = "La Florida",
                    direccion = "Walker Martínez 963"
                ),
                Usuario(
                    run = "195874126",
                    nombre = "Diego",
                    apellidos = "Herrera Castillo",
                    correo = "diego.herrera@profesor.duoc.cl",
                    contrasenha = "diego456",
                    telefono = "978546321",
                    fechaNacimiento = "1990-01-25",
                    tipo = "Cliente",
                    region = "Coquimbo",
                    comuna = "La Serena",
                    direccion = "Balmaceda 741"
                )
            )
            usuarios.forEach { usuarioDao.insertUsuario(it) }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UsuarioDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UsuarioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsuarioDatabase::class.java,
                    "usuario_database"
                )
                    .addCallback(UsuarioDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}