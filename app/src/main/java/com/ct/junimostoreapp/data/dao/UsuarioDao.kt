package com.ct.junimostoreapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ct.junimostoreapp.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE run = :run")
    suspend fun getUsuarioByRun(run: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasenha = :contrasenha")
    suspend fun login(correo: String, contrasenha: String): Usuario?

    @Update
    suspend fun updateUsuario(usuario: Usuario)

    @Delete
    suspend fun removeUsuario(usuario: Usuario)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun getTotalUsuarios(): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE tipo = 'Admin'")
    suspend fun getAdminCount(): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE tipo = 'Cliente'")
    suspend fun getClientCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE run = :run)")
    suspend fun isRunRegistered(run: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE correo = :correo)")
    suspend fun isEmailRegistered(correo: String): Boolean
}
