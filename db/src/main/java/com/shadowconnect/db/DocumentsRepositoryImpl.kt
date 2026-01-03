package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase

class DocumentsRepositoryImpl(
    private val database: HealthShadowDatabase
) : DocumentsRepository {
    override suspend fun createDocumentEntry(
        ownerId: Long,
        externalDocId: Int,
        envelopeId: String,
        documentTitle: String,
        documentStatus: String
    ) : Long {
        return database.professionalDocumentsQueries.createDocument(
            ownerId,
            externalDocId,
            envelopeId,
            documentTitle,
            documentStatus
        ).executeAsOne()
    }
}