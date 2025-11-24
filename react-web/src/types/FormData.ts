import { DayAvailabilityJS } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

export type FormData = {
    // Step 1: Basic Information
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    photo: File | null;
    password: string;
    confirmPassword: string;

    // Step 2: Professional Details
    professionalType: string;
    licenseNumber: string;
    licenseState: string;
    medicalSpecialty: string;
    yearsExperience: string;

    // Step 3: Practice Information
    practiceType: string;
    practiceName: string;
    practiceAddress: string;
    practiceCity: string;
    practiceState: string;
    practiceZip: string;
    titlePosition: string;

    // Step 4: Availability
    availability: DayAvailabilityJS[];  // Uses Kotlin boundary type with JS arrays

    // Step 5: Profile
    bio: string;
    availabilityNotes: string;
};