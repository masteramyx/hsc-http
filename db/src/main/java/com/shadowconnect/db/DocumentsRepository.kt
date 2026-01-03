package com.shadowconnect.db

interface DocumentsRepository {
    suspend fun createDocumentEntry(ownerId: Long,
                            externalDocId: Int,
                            envelopeId: String,
                            documentTitle: String,
                            documentStatus: String): Long
}