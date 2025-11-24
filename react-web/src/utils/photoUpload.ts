import { PhotoUploadResponse } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

/**
 * Uploads a photo to R2 storage and returns the public URL
 * @param file - The File object to upload
 * @returns The public URL of the uploaded photo, or null if upload fails
 */
export async function uploadPhoto(file: File): Promise<string | null> {
    try {
        const form = new FormData();
        form.append('file', file);

        const response = await fetch('/api/upload/photo', {
            method: 'POST',
            body: form
        });

        const result: PhotoUploadResponse = await response.json();

        if (result.success && result.photoUrl) {
            return result.photoUrl;
        } else {
            console.error('Upload failed:', result.error);
            return null;
        }
    } catch (error) {
        console.error('Photo upload failed:', error);
        return null;
    }
}