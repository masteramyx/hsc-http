package com.shadowconnect.db

import com.healthshadow.db.Email_verification_tokens
import com.healthshadow.db.HealthShadowDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EmailVerificationTokenRepositoryImpl(
    private val database: HealthShadowDatabase
) : EmailVerificationTokenRepository {

    override suspend fun createToken(
        userId: Long,
        token: String,
        expiresAt: LocalDateTime
    ): Long = withContext(Dispatchers.IO) {
        database.emailVerificationTokenQueries.createToken(
            user_id = userId,
            token = token,
            expires_at = expiresAt
        ).executeAsOne()
    }

    override suspend fun getTokenByValue(token: String): Email_verification_tokens? =
        withContext(Dispatchers.IO) {
            database.emailVerificationTokenQueries
                .getTokenByValue(token)
                .executeAsOneOrNull()
        }

    override suspend fun markTokenUsed(token: String) = withContext(Dispatchers.IO) {
        database.emailVerificationTokenQueries.markTokenUsed(token)
    }

    override suspend fun getActiveTokenForUser(userId: Long): Email_verification_tokens? =
        withContext(Dispatchers.IO) {
            database.emailVerificationTokenQueries
                .getActiveTokenForUser(userId)
                .executeAsOneOrNull()
        }

    override suspend fun deleteExpiredTokens() = withContext(Dispatchers.IO) {
        database.emailVerificationTokenQueries.deleteExpiredTokens()
    }
}