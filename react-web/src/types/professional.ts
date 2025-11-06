// Professional Types matching backend enums

export enum ProfessionalType {
  MD = 'MD',
  DO = 'DO',
  PA = 'PA',
  NP = 'NP',
  RN = 'RN',
  APRN = 'APRN',
  CNS = 'CNS',
  CRNA = 'CRNA',
  CNM = 'CNM',
  PT = 'PT',
  OT = 'OT',
  SLP = 'SLP',
  LCSW = 'LCSW',
  OTHER = 'OTHER'
}

export enum MedicalSpecialty {
  FAMILY_MEDICINE = 'FAMILY_MEDICINE',
  INTERNAL_MEDICINE = 'INTERNAL_MEDICINE',
  PEDIATRICS = 'PEDIATRICS',
  EMERGENCY_MEDICINE = 'EMERGENCY_MEDICINE',
  SURGERY = 'SURGERY',
  CARDIOLOGY = 'CARDIOLOGY',
  NEUROLOGY = 'NEUROLOGY',
  PSYCHIATRY = 'PSYCHIATRY',
  OBSTETRICS_GYNECOLOGY = 'OBSTETRICS_GYNECOLOGY',
  ORTHOPEDICS = 'ORTHOPEDICS',
  RADIOLOGY = 'RADIOLOGY',
  ANESTHESIOLOGY = 'ANESTHESIOLOGY',
  PATHOLOGY = 'PATHOLOGY',
  DERMATOLOGY = 'DERMATOLOGY',
  ONCOLOGY = 'ONCOLOGY',
  PHYSICAL_THERAPY = 'PHYSICAL_THERAPY',
  OCCUPATIONAL_THERAPY = 'OCCUPATIONAL_THERAPY',
  SPEECH_LANGUAGE_PATHOLOGY = 'SPEECH_LANGUAGE_PATHOLOGY',
  SOCIAL_WORK = 'SOCIAL_WORK',
  OTHER = 'OTHER'
}

export enum PracticeType {
  HOSPITAL = 'HOSPITAL',
  PRIVATE_PRACTICE = 'PRIVATE_PRACTICE',
  CLINIC = 'CLINIC',
  ACADEMIC_MEDICAL_CENTER = 'ACADEMIC_MEDICAL_CENTER',
  URGENT_CARE = 'URGENT_CARE',
  NURSING_HOME = 'NURSING_HOME',
  HOME_HEALTH = 'HOME_HEALTH',
  TELEHEALTH = 'TELEHEALTH',
  OTHER = 'OTHER'
}

export const ProfessionalTypeLabels: Record<ProfessionalType, string> = {
  [ProfessionalType.MD]: 'Doctor of Medicine (MD)',
  [ProfessionalType.DO]: 'Doctor of Osteopathic Medicine (DO)',
  [ProfessionalType.PA]: 'Physician Assistant (PA)',
  [ProfessionalType.NP]: 'Nurse Practitioner (NP)',
  [ProfessionalType.RN]: 'Registered Nurse (RN)',
  [ProfessionalType.APRN]: 'Advanced Practice Registered Nurse (APRN)',
  [ProfessionalType.CNS]: 'Clinical Nurse Specialist (CNS)',
  [ProfessionalType.CRNA]: 'Certified Registered Nurse Anesthetist (CRNA)',
  [ProfessionalType.CNM]: 'Certified Nurse Midwife (CNM)',
  [ProfessionalType.PT]: 'Physical Therapist (PT)',
  [ProfessionalType.OT]: 'Occupational Therapist (OT)',
  [ProfessionalType.SLP]: 'Speech-Language Pathologist (SLP)',
  [ProfessionalType.LCSW]: 'Licensed Clinical Social Worker (LCSW)',
  [ProfessionalType.OTHER]: 'Other Healthcare Professional'
};

export const MedicalSpecialtyLabels: Record<MedicalSpecialty, string> = {
  [MedicalSpecialty.FAMILY_MEDICINE]: 'Family Medicine',
  [MedicalSpecialty.INTERNAL_MEDICINE]: 'Internal Medicine',
  [MedicalSpecialty.PEDIATRICS]: 'Pediatrics',
  [MedicalSpecialty.EMERGENCY_MEDICINE]: 'Emergency Medicine',
  [MedicalSpecialty.SURGERY]: 'Surgery',
  [MedicalSpecialty.CARDIOLOGY]: 'Cardiology',
  [MedicalSpecialty.NEUROLOGY]: 'Neurology',
  [MedicalSpecialty.PSYCHIATRY]: 'Psychiatry',
  [MedicalSpecialty.OBSTETRICS_GYNECOLOGY]: 'Obstetrics & Gynecology',
  [MedicalSpecialty.ORTHOPEDICS]: 'Orthopedics',
  [MedicalSpecialty.RADIOLOGY]: 'Radiology',
  [MedicalSpecialty.ANESTHESIOLOGY]: 'Anesthesiology',
  [MedicalSpecialty.PATHOLOGY]: 'Pathology',
  [MedicalSpecialty.DERMATOLOGY]: 'Dermatology',
  [MedicalSpecialty.ONCOLOGY]: 'Oncology',
  [MedicalSpecialty.PHYSICAL_THERAPY]: 'Physical Therapy',
  [MedicalSpecialty.OCCUPATIONAL_THERAPY]: 'Occupational Therapy',
  [MedicalSpecialty.SPEECH_LANGUAGE_PATHOLOGY]: 'Speech-Language Pathology',
  [MedicalSpecialty.SOCIAL_WORK]: 'Social Work',
  [MedicalSpecialty.OTHER]: 'Other'
};

export const PracticeTypeLabels: Record<PracticeType, string> = {
  [PracticeType.HOSPITAL]: 'Hospital',
  [PracticeType.PRIVATE_PRACTICE]: 'Private Practice',
  [PracticeType.CLINIC]: 'Clinic',
  [PracticeType.ACADEMIC_MEDICAL_CENTER]: 'Academic Medical Center',
  [PracticeType.URGENT_CARE]: 'Urgent Care',
  [PracticeType.NURSING_HOME]: 'Nursing Home',
  [PracticeType.HOME_HEALTH]: 'Home Health',
  [PracticeType.TELEHEALTH]: 'Telehealth',
  [PracticeType.OTHER]: 'Other'
};

export interface ProfessionalRegistrationData {
  // Step 1: Account Creation
  email: string;
  password: string;
  confirmPassword: string;

  // Step 2: Personal Information
  firstName: string;
  lastName: string;
  phone: string;
  photoUrl?: string;

  // Step 3: Credentials
  professionalType: ProfessionalType | '';
  licenseNumber?: string;
  specialization?: MedicalSpecialty | '';
  yearsExperience?: number;
  title?: string;

  // Step 4: Practice Information
  organization?: string;
  practiceType?: PracticeType | '';
  practiceCity?: string;
  practiceState?: string;
  practiceAddress?: string;

  // Step 5: Shadowing Details
  bio?: string;
  specialties?: string;
  studentRequirements?: string;
  availableDays?: string;
  availableTimes?: string;
}