package com.shadowconnect.db

import com.healthshadow.db.Chat_message
import com.healthshadow.db.HealthShadowDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatMessageRepositoryImpl(private val database: HealthShadowDatabase) : ChatMessageRepository {

    override suspend fun insertMessage(userId: Long?, role: String, content: String): Long = withContext(Dispatchers.IO) {
        database.chatMessageQueries.insertMessage(userId, role, content).executeAsOne()
    }

    override suspend fun getMessagesByUserId(userId: Long, limit: Long?): List<Chat_message> = withContext(Dispatchers.IO) {
        if (limit != null) {
            database.chatMessageQueries.getRecentMessagesByUserId(userId, limit).executeAsList()
        } else {
            database.chatMessageQueries.getMessagesByUserId(userId).executeAsList()
        }
    }

    override suspend fun getMessageById(id: Long): Chat_message? = withContext(Dispatchers.IO) {
        database.chatMessageQueries.getMessageById(id).executeAsOneOrNull()
    }

    override suspend fun getAllMessages(): List<Chat_message> = withContext(Dispatchers.IO) {
        database.chatMessageQueries.getAllMessages().executeAsList()
    }
}