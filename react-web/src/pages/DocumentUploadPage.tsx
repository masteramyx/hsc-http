import React, {useState} from 'react';
import {PDFFieldPlacement} from '../components/PDFFieldPlacement';
import {createDocument} from "../utils/documentUpload.ts";
import {DocumentCreatedResponse, FieldPlacement} from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

export const DocumentUploadPage: React.FC = () => {
    const [pdfFile, setPdfFile] = useState<File | null>(null);
    const [fields, setFields] = useState<FieldPlacement[]>([]);

    const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file && file.type === 'application/pdf') {
            setPdfFile(file);
        }
    };

    const handleFieldsChange = (newFields: FieldPlacement[]) => {
        setFields(newFields);
    };

    const handleSubmit = async () => {
        if (!pdfFile) return;
        console.log('Document:', pdfFile?.name);
        console.log('Fields:', fields);
        alert(`Ready to upload! ${fields.length} fields placed`);
        try {
            // todo - create a title???
            const result: DocumentCreatedResponse = await createDocument(pdfFile, fields);
            console.log(`Document upload successfully ${result}`);
        } catch (e) {
            alert(e)
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 p-8">
            <div className="max-w-4xl mx-auto">
                <h1 className="text-3xl font-bold mb-6">Upload Document</h1>

                {/* File Upload */}
                {!pdfFile && (
                    <div className="bg-white rounded-lg shadow-md p-8">
                        <label className="block mb-4">
                            <span className="text-gray-700 font-semibold">Select PDF Document:</span>
                            <input
                                type="file"
                                accept="application/pdf"
                                onChange={handleFileUpload}
                                className="mt-2 block w-full text-sm text-gray-500
                  file:mr-4 file:py-2 file:px-4
                  file:rounded file:border-0
                  file:text-sm file:font-semibold
                  file:bg-blue-50 file:text-blue-700
                  hover:file:bg-blue-100"
                            />
                        </label>
                    </div>
                )}

                {/* PDF Field Placement */}
                {pdfFile && (
                    <>
                        <div className="mb-4 flex justify-between items-center">
                            <h2 className="text-xl font-semibold">Place Signature Fields</h2>
                            <button
                                onClick={() => setPdfFile(null)}
                                className="text-gray-600 hover:text-gray-800"
                            >
                                Change PDF
                            </button>
                        </div>

                        <PDFFieldPlacement file={pdfFile} fields={fields} onFieldsChange={handleFieldsChange}/>

                        {/* Submit Button */}
                        <div className="mt-6 flex justify-end gap-4">
                            <button
                                onClick={() => setPdfFile(null)}
                                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleSubmit}
                                disabled={fields.length === 0}
                                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400"
                            >
                                Upload Document ({fields.length} fields)
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};