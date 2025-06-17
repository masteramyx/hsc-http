package com.shadowconnect.auth

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PasswordValidatorTest {

    @Test
    fun `valid password passes validation`() {
        val result = PasswordValidator.validate("Password123")
        
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `password too short fails validation`() {
        val result = PasswordValidator.validate("Pass1")
        
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.contains("Password must be at least 8 characters long"))
    }

    @Test
    fun `password missing uppercase fails validation`() {
        val result = PasswordValidator.validate("password123")
        
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.contains("Password must contain at least one uppercase letter"))
    }

    @Test
    fun `password missing lowercase fails validation`() {
        val result = PasswordValidator.validate("PASSWORD123")
        
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.contains("Password must contain at least one lowercase letter"))
    }

    @Test
    fun `password missing number fails validation`() {
        val result = PasswordValidator.validate("Password")
        
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.contains("Password must contain at least one number"))
    }

    @Test
    fun `password with multiple violations returns all errors`() {
        val result = PasswordValidator.validate("pass")
        
        assertTrue(result is ValidationResult.Invalid)
        val errors = result.errors
        assertEquals(3, errors.size)
        assertTrue(errors.contains("Password must be at least 8 characters long"))
        assertTrue(errors.contains("Password must contain at least one uppercase letter"))
        assertTrue(errors.contains("Password must contain at least one number"))
    }
}