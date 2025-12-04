package com.shadowconnect.service

import com.shadowconnect.db.EmailVerificationTokenRepository
import com.shadowconnect.db.UserRepository
import com.shadowconnect.utils.logger
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.Base64

class EmailVerificationService(
    private val tokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    private val emailClient: EmailClient
) {
    private val secureRandom = SecureRandom()

    /**
     * Generates a secure random token (URL-safe Base64)
     */
    fun generateToken(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Creates a verification token and sends email
     */
    suspend fun sendVerificationEmail(
        userId: Long,
        email: String,
        firstName: String
    ): Boolean {
        return try {
            // Generate token
            val token = generateToken()
            val expiresAt = LocalDateTime.now().plusHours(24)

            // Save token to database
            tokenRepository.createToken(userId, token, expiresAt)

            // Send email
            emailClient.sendVerificationEmail(email, firstName, token)
        } catch (e: Exception) {
            logger.error("Failed to send verification email for user $userId: ${e.message}", e)
            false
        }
    }

    /**
     * Verifies a token and marks user as verified
     */
    suspend fun verifyEmail(token: String): VerificationResult {
        val tokenRecord = tokenRepository.getTokenByValue(token)
            ?: return VerificationResult.INVALID_TOKEN

        // Check if already used
        if (tokenRecord.used_at != null) {
            return VerificationResult.ALREADY_USED
        }

        // Check expiration
        if (LocalDateTime.now().isAfter(tokenRecord.expires_at)) {
            return VerificationResult.EXPIRED
        }

        // Mark user as verified
        userRepository.markEmailVerified(tokenRecord.user_id)

        // Mark token as used
        tokenRepository.markTokenUsed(token)

        logger.info("User ${tokenRecord.user_id} email verified successfully")
        return VerificationResult.SUCCESS
    }

    /**
     * Resends verification email (creates new token)
     */
    suspend fun resendVerificationEmail(userId: Long): Boolean {
        val user = userRepository.findById(userId) ?: return false
        val userFirstName = userRepository.getUserFirstName(userId) ?: "User"
        return sendVerificationEmail(userId, user.email, userFirstName)
    }
}

enum class VerificationResult {
    SUCCESS,
    INVALID_TOKEN,
    ALREADY_USED,
    EXPIRED
}
