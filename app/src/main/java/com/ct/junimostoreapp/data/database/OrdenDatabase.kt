package com.ct.junimostoreapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ct.junimostoreapp.data.dao.OrdenDao
import com.ct.junimostoreapp.data.model.Orden
import com.ct.junimostoreapp.data.model.ProductoOrden
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Orden::class, ProductoOrden::class], version = 1, exportSchema = false)
abstract class OrdenDatabase : RoomDatabase() {

    abstract fun ordenDao(): OrdenDao

    companion object {
        @Volatile
        private var INSTANCE: OrdenDatabase? = null

        fun getDatabase(context: Context): OrdenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrdenDatabase::class.java,
                    "orden_database"
                )
                .addCallback(OrdenDatabaseCallback(CoroutineScope(Dispatchers.IO)))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class OrdenDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    prePopulateDatabase(it.ordenDao())
                }
            }
        }

        suspend fun prePopulateDatabase(ordenDao: OrdenDao) {
            val ordenes = listOf(
                Orden(numeroOrden = "SO1001", fecha = "03/10/2025", run = "206947953", estadoEnvio = "Entregado", total = 44980),
                Orden(numeroOrden = "SO1002", fecha = "22/10/2025", run = "206947953", estadoEnvio = "Pendiente", total = 31980),
                Orden(numeroOrden = "SO1003", fecha = "21/10/2025", run = "220272430", estadoEnvio = "Enviado", total = 54970),
                Orden(numeroOrden = "SO1004", fecha = "12/10/2025", run = "220272430", estadoEnvio = "Entregado", total = 24990),
                Orden(numeroOrden = "SO1005", fecha = "08/10/2025", run = "199022800", estadoEnvio = "Cancelado", total = 52470),
                Orden(numeroOrden = "SO1006", fecha = "18/10/2025", run = "199022800", estadoEnvio = "Pendiente", total = 17980),
                Orden(numeroOrden = "SO1007", fecha = "15/10/2025", run = "215478967", estadoEnvio = "Entregado", total = 74970),
                Orden(numeroOrden = "SO1008", fecha = "25/10/2025", run = "215478967", estadoEnvio = "Enviado", total = 34980),
                Orden(numeroOrden = "SO1009", fecha = "10/10/2025", run = "198745633", estadoEnvio = "Entregado", total = 29990),
                Orden(numeroOrden = "SO1010", fecha = "28/10/2025", run = "198745633", estadoEnvio = "Pendiente", total = 67470),
                Orden(numeroOrden = "SO1011", fecha = "14/10/2025", run = "225896341", estadoEnvio = "Enviado", total = 44970),
                Orden(numeroOrden = "SO1012", fecha = "05/10/2025", run = "187459632", estadoEnvio = "Entregado", total = 51980),
                Orden(numeroOrden = "SO1013", fecha = "20/10/2025", run = "187459632", estadoEnvio = "Pendiente", total = 24990),
                Orden(numeroOrden = "SO1014", fecha = "17/10/2025", run = "210258740", estadoEnvio = "Enviado", total = 37480),
                Orden(numeroOrden = "SO1015", fecha = "30/10/2025", run = "195874126", estadoEnvio = "Pendiente", total = 59970)
            )
            ordenes.forEach { ordenDao.insertOrden(it) }

            val productos = listOf(
                ProductoOrden(ordenNumeroOrden = "SO1001", codigo = "PE001", nombre = "Peluche De Krobus", cantidad = 1, precio = 19990),
                ProductoOrden(ordenNumeroOrden = "SO1001", codigo = "AC002", nombre = "Taza Stardew Valley", cantidad = 2, precio = 7990),
                ProductoOrden(ordenNumeroOrden = "SO1001", codigo = "AC003", nombre = "Pins Stardew Valley", cantidad = 3, precio = 2500),
                ProductoOrden(ordenNumeroOrden = "SO1002", codigo = "PP001", nombre = "Polera Stardew Valley Personalizada", cantidad = 1, precio = 11990),
                ProductoOrden(ordenNumeroOrden = "SO1002", codigo = "GU001", nombre = "Guía Ilustrada De Cultivos", cantidad = 1, precio = 24990),
                ProductoOrden(ordenNumeroOrden = "SO1003", codigo = "JM001", nombre = "Stardew Valley Juego De Mesa", cantidad = 1, precio = 29990),
                ProductoOrden(ordenNumeroOrden = "SO1003", codigo = "DE001", nombre = "Póster Stardew Valley", cantidad = 1, precio = 14990),
                ProductoOrden(ordenNumeroOrden = "SO1003", codigo = "MD001", nombre = "Pack De Texturas HD", cantidad = 1, precio = 9990),
                ProductoOrden(ordenNumeroOrden = "SO1004", codigo = "GU001", nombre = "Guía Ilustrada De Cultivos", cantidad = 1, precio = 24990),
                ProductoOrden(ordenNumeroOrden = "SO1005", codigo = "DE002", nombre = "Almohada Pollo De Stardew Valley", cantidad = 2, precio = 15990),
                ProductoOrden(ordenNumeroOrden = "SO1005", codigo = "AC001", nombre = "Llavero Stardew Valley", cantidad = 1, precio = 5990),
                ProductoOrden(ordenNumeroOrden = "SO1005", codigo = "AC003", nombre = "Pins Stardew Valley", cantidad = 5, precio = 2500),
                ProductoOrden(ordenNumeroOrden = "SO1006", codigo = "AC001", nombre = "Llavero Stardew Valley", cantidad = 2, precio = 5990),
                ProductoOrden(ordenNumeroOrden = "SO1006", codigo = "AC002", nombre = "Taza Stardew Valley", cantidad = 1, precio = 7990),
                ProductoOrden(ordenNumeroOrden = "SO1007", codigo = "PE001", nombre = "Peluche De Krobus", cantidad = 1, precio = 19990),
                ProductoOrden(ordenNumeroOrden = "SO1007", codigo = "JM001", nombre = "Stardew Valley Juego De Mesa", cantidad = 1, precio = 29990),
                ProductoOrden(ordenNumeroOrden = "SO1007", codigo = "DE001", nombre = "Póster Stardew Valley", cantidad = 1, precio = 14990),
                ProductoOrden(ordenNumeroOrden = "SO1007", codigo = "AC003", nombre = "Pins Stardew Valley", cantidad = 4, precio = 2500),
                ProductoOrden(ordenNumeroOrden = "SO1008", codigo = "PP001", nombre = "Polera Stardew Valley Personalizada", cantidad = 2, precio = 11990),
                ProductoOrden(ordenNumeroOrden = "SO1008", codigo = "AC001", nombre = "Llavero Stardew Valley", cantidad = 2, precio = 5990),
                ProductoOrden(ordenNumeroOrden = "SO1009", codigo = "JM001", nombre = "Stardew Valley Juego De Mesa", cantidad = 1, precio = 29990),
                ProductoOrden(ordenNumeroOrden = "SO1010", codigo = "PE001", nombre = "Peluche De Krobus", cantidad = 2, precio = 19990),
                ProductoOrden(ordenNumeroOrden = "SO1010", codigo = "DE002", nombre = "Almohada Pollo De Stardew Valley", cantidad = 1, precio = 15990),
                ProductoOrden(ordenNumeroOrden = "SO1010", codigo = "GU001", nombre = "Guía Ilustrada De Cultivos", cantidad = 1, precio = 24990),
                ProductoOrden(ordenNumeroOrden = "SO1011", codigo = "DE001", nombre = "Póster Stardew Valley", cantidad = 1, precio = 14990),
                ProductoOrden(ordenNumeroOrden = "SO1011", codigo = "DE002", nombre = "Almohada Pollo De Stardew Valley", cantidad = 1, precio = 15990),
                ProductoOrden(ordenNumeroOrden = "SO1011", codigo = "PP001", nombre = "Polera Stardew Valley Personalizada", cantidad = 1, precio = 11990),
                ProductoOrden(ordenNumeroOrden = "SO1012", codigo = "GU001", nombre = "Guía Ilustrada De Cultivos", cantidad = 1, precio = 24990),
                ProductoOrden(ordenNumeroOrden = "SO1012", codigo = "JM001", nombre = "Stardew Valley Juego De Mesa", cantidad = 1, precio = 29990),
                ProductoOrden(ordenNumeroOrden = "SO1013", codigo = "DE002", nombre = "Almohada Pollo De Stardew Valley", cantidad = 1, precio = 15990),
                ProductoOrden(ordenNumeroOrden = "SO1013", codigo = "AC002", nombre = "Taza Stardew Valley", cantidad = 1, precio = 7990),
                ProductoOrden(ordenNumeroOrden = "SO1014", codigo = "PP001", nombre = "Polera Stardew Valley Personalizada", cantidad = 2, precio = 11990),
                ProductoOrden(ordenNumeroOrden = "SO1014", codigo = "AC003", nombre = "Pins Stardew Valley", cantidad = 5, precio = 2500),
                ProductoOrden(ordenNumeroOrden = "SO1015", codigo = "PE001", nombre = "Peluche De Krobus", cantidad = 1, precio = 19990),
                ProductoOrden(ordenNumeroOrden = "SO1015", codigo = "JM001", nombre = "Stardew Valley Juego De Mesa", cantidad = 1, precio = 29990),
                ProductoOrden(ordenNumeroOrden = "SO1015", codigo = "MD001", nombre = "Pack De Texturas HD", cantidad = 1, precio = 9990)
            )
            ordenDao.insertProductosOrden(productos)
        }
    }
}