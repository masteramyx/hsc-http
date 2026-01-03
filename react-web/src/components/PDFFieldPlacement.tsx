import React, { useState } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import Draggable from 'react-draggable';
import {FieldPlacement, FieldType} from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';
// Styles interactive PDF elements - links, formfields, highlights
import 'react-pdf/dist/Page/AnnotationLayer.css';
// Styles selectable text layer on top of PDF - search, copy/paste, text selection, text positioning
import 'react-pdf/dist/Page/TextLayer.css';

// Configure PDF.js worker from CDN
pdfjs.GlobalWorkerOptions.workerSrc = `//unpkg.com/pdfjs-dist@${pdfjs.version}/build/pdf.worker.min.mjs`;


interface PDFFieldPlacementProps {
  file: File;
  fields: FieldPlacement[];
  onFieldsChange: (fields: FieldPlacement[]) => void;
}

export const PDFFieldPlacement: React.FC<PDFFieldPlacementProps> = ({ file, fields, onFieldsChange }) => {
  const [numPages, setNumPages] = useState<number>(0);
  const [pageWidth] = useState<number>(600);
  const [selectedFieldType, setSelectedFieldType] = useState<FieldType| null>(null);

  const onDocumentLoadSuccess = ({ numPages }: { numPages: number }) => {
    setNumPages(numPages);
  };

  const addField = (pageNumber: number, relativeX: number, relativeY: number) => {
    // Don't place field if no type selected
    if (!selectedFieldType) return;

    // Calculate field dimensions FIRST
    const fieldWidth = selectedFieldType === FieldType.SIGNATURE ? 200 : 150;
    const fieldHeight = selectedFieldType === FieldType.SIGNATURE ? 56 : 40;

    // Store in parent-relative coordinates
    const newField: FieldPlacement = new FieldPlacement(
      `field-${Date.now()}`,
      selectedFieldType,
      pageNumber,
      relativeX,
      relativeY,
      fieldWidth,
      fieldHeight,
      );

    const updatedFields = [...fields, newField];
    onFieldsChange(updatedFields);

    // Auto-deselect after placing field
    setSelectedFieldType(null);
  };

  const handleFieldDrag = (fieldId: string, data: { x: number; y: number }) => {
    const updatedFields: FieldPlacement[] = fields.map(field =>
        fieldId === field.id
            ? field.copy(field.id,field.type, field.page, data.x, data.y, field.width, field.height)
            : field
    );

    onFieldsChange(updatedFields);
  };

  const removeField = (fieldId: string) => {
    const updatedFields = fields.filter(f => f.id !== fieldId);
    onFieldsChange(updatedFields);
  };

  return (
    <div className="pdf-field-placement">
      {/* Field Type Selector */}
      <div className="mb-4 p-4 bg-gray-100 rounded">
        <div className="flex items-center justify-between mb-2">
          <h3 className="font-semibold">
            {selectedFieldType ? `Click PDF to place ${selectedFieldType} field` : 'Select a field type to place:'}
          </h3>
          {selectedFieldType && (
            <button
              onClick={() => setSelectedFieldType(null)}
              className="text-sm text-red-600 hover:text-red-800"
            >
              Cancel
            </button>
          )}
        </div>
        <div className="flex gap-2">
          <button
            onClick={() => setSelectedFieldType(selectedFieldType === FieldType.SIGNATURE ? null : FieldType.SIGNATURE)}
            className={`px-4 py-2 rounded border-2 transition ${
              selectedFieldType === FieldType.SIGNATURE
                ? 'bg-blue-600 text-white border-blue-700'
                : 'bg-white border-gray-300 hover:border-blue-400'
            }`}
          >
            Signature
          </button>
          <button
            onClick={() => setSelectedFieldType(selectedFieldType === FieldType.DATE ? null : FieldType.DATE)}
            className={`px-4 py-2 rounded border-2 transition ${
              selectedFieldType === FieldType.DATE
                ? 'bg-blue-600 text-white border-blue-700'
                : 'bg-white border-gray-300 hover:border-blue-400'
            }`}
          >
            Date
          </button>
          <button
            onClick={() => setSelectedFieldType(selectedFieldType === FieldType.TEXT ? null : FieldType.TEXT)}
            className={`px-4 py-2 rounded border-2 transition ${
              selectedFieldType === FieldType.TEXT
                ? 'bg-blue-600 text-white border-blue-700'
                : 'bg-white border-gray-300 hover:border-blue-400'
            }`}
          >
            Text
          </button>
        </div>
      </div>

      {/* PDF Viewer */}
      <div className="border rounded bg-gray-50 p-4">
        <Document file={file} onLoadSuccess={onDocumentLoadSuccess}>
          {Array.from(new Array(numPages), (_, index) => (
            <div
              key={`page_${index + 1}`}
              className="relative mb-4"
              onClick={(e) => {
                if (!selectedFieldType) return;

                // Calculate position relative to currentTarget (our parent div)
                const rect = e.currentTarget.getBoundingClientRect();
                const relativeX = e.clientX - rect.left;
                const relativeY = e.clientY - rect.top;

                addField(index + 1, relativeX, relativeY);
              }}
            >
              <Page pageNumber={index + 1} width={pageWidth} />

              {/* Render fields for this page */}
              {fields
                .filter(field => field.page === index + 1)
                .map(field => (
                  <Draggable
                    key={field.id}
                    defaultPosition={{ x: field.x, y: field.y }}
                    onStop={(e, data) => {
                        console.log(`Drag Event: ${e}`);
                        handleFieldDrag(field.id, data)
                        }
                    }
                    cancel=".delete-button"
                  >
                    <div
                      className="absolute border-2 border-blue-500 bg-blue-100 bg-opacity-50 cursor-move"
                      style={{
                        left: 0,
                        top: 0,
                        width: `${field.width}px`,
                        height: `${field.height}px`,
                        zIndex: 10,
                      }}
                    >
                      <div className="flex items-center justify-between h-full px-2 text-xs">
                        <span>{field.type.displayName}</span>
                        <button
                          onMouseDown={(e) => e.stopPropagation()}
                          onClick={(e) => {
                            e.stopPropagation();
                            removeField(field.id);
                          }}
                          className="delete-button text-red-600 hover:text-red-800 font-bold text-lg cursor-pointer"
                        >
                          ×
                        </button>
                      </div>
                    </div>
                  </Draggable>
                ))}
            </div>
          ))}
        </Document>
      </div>
    </div>
  );
};