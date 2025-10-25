package com.shadowconnect.routes

import com.shadowconnect.db.ChatMessageRepositoryImpl
import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.service.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.flow.catch
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.onEach

@Serializable
data class ChatRequest(
    val message: String
)

fun Route.chatRouting() {
    val database = DatabaseFactory.getDatabase()
    val messageRepository = ChatMessageRepositoryImpl(database)
    val chatService = ChatService()

    post("/api/chat") {
        try {
            val chatRequest = call.receive<ChatRequest>()

            // Log user message to database
            try {
                messageRepository.insertMessage(null, "user", chatRequest.message)
                println("ChatRoutes: Saved user message to database")
            } catch (e: Exception) {
                println("ChatRoutes: Error saving user message to database: ${e.message}")
            }

            // Set up Server-Sent Events response
            call.response.headers.append("Content-Type", "text/event-stream")
            call.response.headers.append("Cache-Control", "no-cache")
            call.response.headers.append("Connection", "keep-alive")
            call.response.headers.append("X-Accel-Buffering", "no")

            val fullResponse = StringBuilder()

            call.respondBytesWriter(contentType = ContentType.Text.EventStream) {
                chatService.streamChat2(chatRequest.message, null)
                    .catch { e ->
                        writeStringUtf8("data: Error: ${e.message}\n\n")
                        flush()
                    }
                    .onEach { chunk ->  fullResponse.append(chunk) }
                    .collect { chunk ->
                        // Replace newlines in the chunk with escaped newlines for SSE format
                        val escapedChunk = chunk.replace("\n", "\\n")
                        writeStringUtf8("data: $escapedChunk\n\n")
                        flush()
                    }

                if (fullResponse.isNotEmpty()) {
                    messageRepository.insertMessage(null, "assistant", fullResponse.toString())
                }
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error occurred"))
            )
        }
    }
}
