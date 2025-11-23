package com.ct.junimostoreapp.data.repository

import com.ct.junimostoreapp.data.dao.UsuarioDao
import com.ct.junimostoreapp.data.model.Usuario
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val usuarioDao: UsuarioDao) {

    fun getUsuarios(): Flow<List<Usuario>> = usuarioDao.getUsuarios()

    suspend fun login(username: String, password: String): Usuario? {
        return usuarioDao.login(username, password)
    }

    suspend fun registrar(usuario: Usuario) {
        if (!isRunRegistered(usuario.run) && !isEmailRegistered(usuario.correo)) {
            usuarioDao.insertUsuario(usuario)
        }
    }

    suspend fun getUsuarioPorRut(rut: String): Usuario? {
        val cleanRut = rut.replace(Regex("[.-]"), "")
        return usuarioDao.getUsuarioByRun(cleanRut)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.updateUsuario(usuario)
    }

    suspend fun isRunRegistered(run: String): Boolean {
        val cleanRun = run.replace(Regex("[.-]"), "")
        return usuarioDao.isRunRegistered(cleanRun)
    }

    suspend fun isEmailRegistered(email: String): Boolean {
        return usuarioDao.isEmailRegistered(email)
    }

    suspend fun removeUsuario(usuario: Usuario) {
        usuarioDao.removeUsuario(usuario)
    }

    suspend fun getTotalUsuarios(): Int = usuarioDao.getTotalUsuarios()

    suspend fun getAdminCount(): Int = usuarioDao.getAdminCount()

    suspend fun getClientCount(): Int = usuarioDao.getClientCount()
}
