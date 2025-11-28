package com.shadowconnect.service

import com.resend.Resend
import com.resend.core.exception.ResendException
import com.resend.services.emails.model.SendEmailRequest
import com.shadowconnect.utils.logger

class EmailClient {
    private val resendApiKey = System.getenv("RESEND_API_KEY")
        ?: throw IllegalStateException("RESEND_API_KEY environment variable not set")

    private val fromEmail = System.getenv("RESEND_FROM_EMAIL")
        ?: "ShadowConnect <noreply@shadowconnects.com>"

    private val appUrl = System.getenv("APP_URL")
        ?: "http://localhost:8080"

    private val resend = Resend(resendApiKey)

    /**
     * Sends verification email with token link
     */
    fun sendVerificationEmail(
        toEmail: String,
        firstName: String,
        verificationToken: String
    ): Boolean {
        return try {
            val verificationUrl = "$appUrl/verify-email?token=$verificationToken"

            val html = buildVerificationEmailHtml(firstName, verificationUrl)

            val params = SendEmailRequest.builder()
                .from(fromEmail)
                .to(toEmail)
                .subject("Verify your ShadowConnect email address")
                .html(html)
                .build()

            val response = resend.emails().send(params)
            logger.info("Verification email sent to $toEmail, message ID: ${response.id}")
            true
        } catch (e: ResendException) {
            logger.error("Failed to send verification email to $toEmail: ${e.message}", e)
            false
        }
    }

    /**
     * Sends reminder to verify email
     */
    fun sendVerificationReminder(
        toEmail: String,
        firstName: String,
        verificationToken: String
    ): Boolean {
        return try {
            val verificationUrl = "$appUrl/verify-email?token=$verificationToken"

            val html = buildReminderEmailHtml(firstName, verificationUrl)

            val params = SendEmailRequest.builder()
                .from(fromEmail)
                .to(toEmail)
                .subject("Reminder: Verify your ShadowConnect email")
                .html(html)
                .build()

            resend.emails().send(params)
            logger.info("Verification reminder sent to $toEmail")
            true
        } catch (e: ResendException) {
            logger.error("Failed to send reminder to $toEmail: ${e.message}", e)
            false
        }
    }

    private fun buildVerificationEmailHtml(firstName: String, verificationUrl: String): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2>Welcome to ShadowConnect, $firstName!</h2>
                    <p>Thank you for registering. Please verify your email address to complete your account setup.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="$verificationUrl"
                           style="background-color: #2563eb; color: white; padding: 12px 24px;
                                  text-decoration: none; border-radius: 6px; display: inline-block;">
                            Verify Email Address
                        </a>
                    </div>
                    <p style="color: #666; font-size: 14px;">
                        This link will expire in 24 hours. If you didn't create an account,
                        you can safely ignore this email.
                    </p>
                    <p style="color: #666; font-size: 14px;">
                        If the button doesn't work, copy and paste this link into your browser:<br>
                        <a href="$verificationUrl">$verificationUrl</a>
                    </p>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun buildReminderEmailHtml(firstName: String, verificationUrl: String): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2>Hi $firstName,</h2>
                    <p>We noticed you haven't verified your email address yet.</p>
                    <p>Verifying your email helps secure your account and ensures you receive important updates.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="$verificationUrl"
                           style="background-color: #2563eb; color: white; padding: 12px 24px;
                                  text-decoration: none; border-radius: 6px; display: inline-block;">
                            Verify Email Address
                        </a>
                    </div>
                    <p style="color: #666; font-size: 14px;">
                        This link will expire in 24 hours.
                    </p>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}