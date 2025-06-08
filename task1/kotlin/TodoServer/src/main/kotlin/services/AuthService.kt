package com.hacker.services

import com.hacker.auth.JwtConfig
import com.hacker.models.UsersTable
import com.hacker.repository.UserRepository
import java.security.MessageDigest

object AuthService {
    fun login(username: String, password: String): String? {
        val userRow = UserRepository.findUserByUsername(username)
        return if (userRow != null && hash(password) == userRow[UsersTable.password]) {
            JwtConfig.generateToken(username,userRow[UsersTable.passwordVersion])
        } else null
    }

    fun changePassword(username: String, oldPassword: String, newPassword: String): Boolean {
        val userRow = UserRepository.findUserByUsername(username)
        return if (userRow != null && hash(oldPassword) == userRow[UsersTable.password]) {
            UserRepository.updatePassword(username, hash(newPassword))
        } else false
    }

    fun initAdminUser() {
        if (!UserRepository.userExists("admin")) {
            UserRepository.createUser("admin", hash("admin"))
            println("✅ Default admin user created (username: admin, password: admin)")
        } else {
            println("ℹ️ Admin user already exists.")
        }
    }

    private fun hash(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
