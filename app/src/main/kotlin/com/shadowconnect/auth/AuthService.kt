package com.shadowconnect.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.healthshadow.db.Users
import com.shadowconnect.db.UserRepository

class AuthService(private val userRepository: UserRepository) {
    
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }
    
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
    
    suspend fun authenticateUser(email: String, password: String): Users? {
        val user = userRepository.findByEmail(email) ?: return null
        return if (verifyPassword(password, user.password_hash)) user else null
    }
}