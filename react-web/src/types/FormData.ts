import { DayOfWeek, TimeRange } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

// TypeScript version for React state (uses plain JS arrays)
// This mirrors the Kotlin DayAvailability but works with React's array methods
export type DayAvailability = {
    day: DayOfWeek;
    timeRanges: TimeRange[];  // Plain JS array, not KList
};

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
    availability: DayAvailability[];  // Uses our TypeScript version

    // Step 5: Profile
    bio: string;
    availabilityNotes: string;
};