package com.ct.junimostoreapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ct.junimostoreapp.R
import com.ct.junimostoreapp.data.dao.ProductoDao
import com.ct.junimostoreapp.data.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database( // Anotación que define la base de datos de Room.
    entities = [Producto::class], // Lista de todas las entidades (tablas) que pertenecen a esta base de datos.
    version = 1, // Versión de la base de datos, útil para migraciones.
    exportSchema = false  // Evita que Room exporte el esquema de la base de datos a un archivo JSON.
)
abstract class ProductoDatabase : RoomDatabase() { // Clase abstracta que representa la base de datos.
    abstract fun productoDao(): ProductoDao // Método abstracto que devuelve el DAO para la entidad Producto.

    private class ProductoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.productoDao())
                }
            }
        }

        suspend fun populateDatabase(productoDao: ProductoDao) {
            val productos = listOf(
                Producto(
                    codigo = "AC001",
                    categoria = "Accesorios",
                    nombre = "Llavero Stardew Valley",
                    descripcion = "Llavero temático de Stardew Valley, perfecto para llevar contigo a todas partes.",
                    precio = 5990,
                    stock = 76,
                    stockCritico = 30,
                    imagen = R.drawable.llavero_stardew
                ),
                Producto(
                    codigo = "AC002",
                    categoria = "Accesorios",
                    nombre = "Taza Stardew Valley",
                    descripcion = "Taza de cerámica con diseño exclusivo de Stardew Valley, ideal para tu bebida favorita.",
                    precio = 7990,
                    stock = 48,
                    stockCritico = 20,
                    imagen = R.drawable.taza_stardew_valley
                ),
                Producto(
                    codigo = "AC003",
                    categoria = "Accesorios",
                    nombre = "Pins Stardew Valley",
                    descripcion = "Set de pins coleccionables con personajes y elementos de Stardew Valley.",
                    precio = 2500,
                    stock = 162,
                    stockCritico = 100,
                    imagen = R.drawable.pins_stardew_valley
                ),
                Producto(
                    codigo = "DE001",
                    categoria = "Decoración",
                    nombre = "Póster Stardew Valley (Edición Granja)",
                    descripcion = "Póster decorativo de alta calidad con diseño inspirado en la granja de Stardew Valley.",
                    precio = 14990,
                    stock = 9,
                    stockCritico = 10,
                    imagen = R.drawable.poster_granja_stardew
                ),
                Producto(
                    codigo = "DE002",
                    categoria = "Decoración",
                    nombre = "Almohada Pollo De Stardew Valley",
                    descripcion = "Almohada decorativa con forma del icónico pollo de Stardew Valley.",
                    precio = 15990,
                    stock = 29,
                    stockCritico = 30,
                    imagen = R.drawable.almohada_pollo_stardew
                ),
                Producto(
                    codigo = "GU001",
                    categoria = "Guías",
                    nombre = "Guía Ilustrada De Cultivos",
                    descripcion = "Guía impresa a todo color con ilustraciones y consejos para cultivos en Stardew Valley.",
                    precio = 24990,
                    stock = 63,
                    stockCritico = 20,
                    imagen = R.drawable.guia_cultivos_stardew
                ),
                Producto(
                    codigo = "JM001",
                    categoria = "Juego De Mesa",
                    nombre = "Stardew Valley Juego De Mesa",
                    descripcion = "Versión física del juego de mesa basado en el mundo de Stardew Valley.",
                    precio = 29990,
                    stock = 55,
                    stockCritico = 30,
                    imagen = R.drawable.juego_mesa_stardew
                ),
                Producto(
                    codigo = "MD001",
                    categoria = "Mods Digitales",
                    nombre = "Pack De Texturas HD",
                    descripcion = "Paquete digital de mods para mejorar las texturas del juego a alta definición.",
                    precio = 9990,
                    stock = 241,
                    stockCritico = 100,
                    imagen = R.drawable.pack_texturas_hd
                ),
                Producto(
                    codigo = "PE001",
                    categoria = "Peluches",
                    nombre = "Peluche De Krobus",
                    descripcion = "Suave y adorable peluche de Krobus, ideal para fans y coleccionistas.",
                    precio = 19990,
                    stock = 3,
                    stockCritico = 20,
                    imagen = R.drawable.peluche_krobus
                ),
                Producto(
                    codigo = "PP001",
                    categoria = "Polera Personalizada",
                    nombre = "Polera Stardew Valley Personalizada (Edición Limitada)",
                    descripcion = "Polera de edición limitada con diseño personalizado inspirado en Stardew Valley.",
                    precio = 11990,
                    stock = 12,
                    stockCritico = 20,
                    imagen = R.drawable.polera_stardew_personalizada
                )
            )
            productos.forEach { productoDao.insertarProducto(it) }
        }
    }

    companion object { // Objeto compañero para implementar el patrón Singleton y obtener una única instancia de la base de datos.
        @Volatile // Anotación que asegura que los cambios en la instancia sean visibles para todos los hilos.
        private var INSTANCE: ProductoDatabase? = null // Variable que contendrá la instancia única de la base de datos.

        fun getDatabase(context: Context, scope: CoroutineScope): ProductoDatabase { // Método para obtener la instancia de la base de datos.
            return INSTANCE ?: synchronized(this) { // Si la instancia no existe, se crea de forma segura para evitar problemas de concurrencia.
                val instance = Room.databaseBuilder( // Construye la base de datos.
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                )
                    .addCallback(ProductoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
