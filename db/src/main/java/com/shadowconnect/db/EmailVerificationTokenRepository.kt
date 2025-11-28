package com.shadowconnect.db

import com.healthshadow.db.Email_verification_tokens
import java.time.LocalDateTime

interface EmailVerificationTokenRepository {
    suspend fun createToken(userId: Long, token: String, expiresAt: LocalDateTime): Long
    suspend fun getTokenByValue(token: String): Email_verification_tokens?
    suspend fun markTokenUsed(token: String)
    suspend fun getActiveTokenForUser(userId: Long): Email_verification_tokens?
    suspend fun deleteExpiredTokens()
}