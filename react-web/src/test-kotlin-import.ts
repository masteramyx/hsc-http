// Test file to verify Kotlin/JS ES module imports work
// @ts-ignore
import { ProfessionalType, MedicalSpecialty, PracticeType, USStates } from '../../shared/build/dist/js/productionLibrary/hsc-http-shared.mjs';

// Test accessing the exports
console.log('=== Testing Kotlin/JS imports ===');
console.log('ProfessionalType:', ProfessionalType);
console.log('ProfessionalType.MD:', ProfessionalType.MD);
console.log('ProfessionalType.MD.displayName:', ProfessionalType.MD.displayName);
console.log('MedicalSpecialty:', MedicalSpecialty);
console.log('PracticeType:', PracticeType);
console.log('USStates:', USStates);
console.log('USStates.getInstance():', USStates.getInstance());
console.log('USStates.getInstance().ALL:', USStates.getInstance().ALL);
console.log('=== Test complete ===');

export {};