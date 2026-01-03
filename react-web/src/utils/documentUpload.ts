import { DocumentCreatedResponse, FieldPlacement } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

export async function createDocument(file: File, fields: FieldPlacement[]) : Promise<DocumentCreatedResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', 'document title');
    formData.append('fields', FieldPlacement.Companion.toJsonArray(fields));

    const response = await fetch('/api/v1/documents/create', {
        method: 'POST',
        credentials: 'include',
        body: formData
    });

    if(!response.ok) {
        throw new Error('Failed to create document');
    }

    const jsonText = await response.text();
    const result: DocumentCreatedResponse = DocumentCreatedResponse.Companion.fromJson(jsonText);
    console.log(`Document created: ${result}`);
    return result;
}