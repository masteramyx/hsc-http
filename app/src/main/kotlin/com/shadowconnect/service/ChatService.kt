package com.shadowconnect.service

import com.shadowconnect.httpClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class SSEEvent(
    val event: String?,  // "message_start", "content_block_delta", etc.
    val data: String     // The JSON payload after "data: "
)

@Serializable
data class ClaudeMessage(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val messages: List<ClaudeMessage>,
    val stream: Boolean = true
)

@Serializable
data class ClaudeResponse(
    val id: String? = null,
    val type: String? = null,
    val role: String? = null,
    val content: List<ClaudeContent>? = null,
    val model: String? = null,
    val stop_reason: String? = null,
    val delta: ClaudeDelta? = null
)

@Serializable
data class ClaudeContent(
    val type: String,
    val text: String? = null
)

@Serializable
data class ClaudeDelta(
    val type: String? = null,
    val text: String? = null,
    val stop_reason: String? = null
)

class ChatService() {
    private val apiKey = System.getenv("ANTHROPIC_API_KEY")
        ?: throw IllegalStateException("ANTHROPIC_API_KEY environment variable not set")



    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private suspend fun streamSSE(url: String, requestBody: String): Flow<SSEEvent> = flow {
        // make request
        val response = httpClient.post(url) {
            headers {
                append("x-api-key",apiKey)
                append("anthropic-version","2023-06-01")
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        // throw exception if error
        if(!response.status.isSuccess()) {
            throw Exception(response.status.description)
        }

        // open byte stream channel
        val channel: ByteReadChannel = response.bodyAsChannel()
        var currentEvent: String? = null

        // while chanel is not closed
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break

            // Handle non-streaming responses (complete JSON object)
            if (line.startsWith("{") && line.contains("\"content\"")) {
                // This is a complete response, emit it as a single SSEEvent
                emit(SSEEvent(event = "message", data = line))
                break
            }

            // parse out event and data and emit structured data from string
            when {
                line.startsWith("event: ") -> {
                    currentEvent = line.substring(7).trim()
                }
                line.startsWith("data: ") -> {
                    val data = line.substring(6).trim()
                    if(data == "[DONE]") break

                    emit(SSEEvent(event = currentEvent, data = data))
                    // reset for next event
                    currentEvent = null
                }
            }
        }
    }

    private suspend fun streamClaudeResponse(message: String): Flow<ClaudeResponse> {
        val request = ClaudeRequest(
            model = "claude-3-7-sonnet-20250219",
            max_tokens = 1024,
            messages = listOf(
                ClaudeMessage(role = "user", content = message),
            ),
            stream = true
        )

        val requestBody = json.encodeToString(ClaudeRequest.serializer(), request)


        return streamSSE("https://api.anthropic.com/v1/messages", requestBody)
            .mapNotNull { event ->
                try {
                    json.decodeFromString<ClaudeResponse>(event.data)
                } catch (e: Exception) {
                    println("Failed to parse: ${event.data}")
                    null //skip this event
                }
            }
            .filter { it.type == "content_block_delta" || it.type == "message" }
    }

    suspend fun streamChat2(userMessage: String, userId: Long? = null): Flow<String> =
        streamClaudeResponse(userMessage)
            .mapNotNull { response ->
                when (response.type) {
                    "content_block_delta" -> response.delta?.text
                    "message" -> response.content?.firstOrNull()?.text
                    else -> null
                }
            }

    fun close() {
        httpClient.close()
    }
}
