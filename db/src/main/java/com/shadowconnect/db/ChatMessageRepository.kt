package com.shadowconnect.db

import com.healthshadow.db.Chat_message

interface ChatMessageRepository {
    suspend fun insertMessage(userId: Long?, role: String, content: String): Long
    suspend fun getMessagesByUserId(userId: Long, limit: Long? = null): List<Chat_message>
    suspend fun getMessageById(id: Long): Chat_message?
    suspend fun getAllMessages(): List<Chat_message>
}